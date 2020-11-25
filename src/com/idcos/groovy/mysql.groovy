package com.idcos.groovy.test


import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.idcos.cmp.core.biz.common.template.CommonBizException
import com.idcos.cmp.core.biz.form.BootPmUpdateAssignedForm
import com.idcos.cmp.core.biz.manager.CidrBlockManager
import com.idcos.cmp.core.biz.manager.CloudBootManager
import com.idcos.cmp.core.biz.resourcepool.CmpResourcePoolManager
import com.idcos.cmp.core.biz.resourcepool.form.CmpResourcePoolExcuteForm
import com.idcos.cmp.core.biz.util.SpringContextUtil
import org.apache.commons.lang3.StringUtils

//先定义网络区域与网段的对应关系
def networkArea = [
        "area1": ["3"]
]


def serviceAccountId = 6

//定义硬件模板
def hardwareTemplateId = ''

//定义系统模板
def installType = 'pxe'


//定义系统模板和操作系统，服务器类型，cpu架构的关系
def systemTemplate = [
        "26":[
                "os":"KyLinV10",
                "serverType":"计算型服务器",
                "cpuArch":["ARM","海光"]
        ],
        "25":[
                "os":"SLES12SP2",
                "serverType":"计算型服务器",
                "cpuArch":["X86"]
        ],
        "26":[
                "os":"KyLinV10",
                "serverType":"存储型服务器",
                "cpuArch":["ARM","海光"]
        ]
]

def cmpResourcePoolManager = SpringContextUtil.getBean(CmpResourcePoolManager.class);

//如果是蓝图数据
if (params.parameters.catalogItem.source == 'blueprint') {
    //获取蓝图中数据
    def steps = params.parameters.catalogItem.metadata.steps;
    //获取前端请求数据
    def requestedItemInput = params.parameters.requestedItemInput
    //后期需要物理机和虚拟机一起申请，需要进行适配改进
    //TODO
    steps.each { step ->
        def networkAreaParam = "";
        def os = ""
        def serverType = ""
        def num = ""
        def hostNameSuffix = ""
        def memberId = ""
        def application = ""
        def cpuArch = ""

        if (step.operation == 'provision' && step.isAllowBatchOperation) {
            def rbInstCodeNum = step.rbInstCode + '_num'
            def rbInstCodeNetworkArea = step.rbInstCode + '_networkArea'
            def rbInstCodeOs = step.rbInstCode + '_os'
            def rbInstCodeServerType = step.rbInstCode + '_serverType'
            def rbInstCodeHostNameSuffix = step.rbInstCode + '_hostNameSuffix'
            def rbInstCodeApplication = step.rbInstCode + '_application'
            def rbInstCodeCpuArch = step.rbInstCode + '_cpuArch'


            //如果是多实例,找到主机数量
            requestedItemInput.each { item ->
                // rbInstCodeNum
                if (item.name == rbInstCodeNum) {
                    num = Integer.parseInt(item.value)
                }
                // rbInstCodeNetworkArea
                if (item.name == rbInstCodeNetworkArea) {
                    networkAreaParam = item.value
                }
                // rbInstCodeOs
                if (item.name == rbInstCodeOs) {
                    os = item.value
                }
                // rbInstCodeServerType
                if (item.name == rbInstCodeServerType) {
                    serverType = item.value
                }
                // rbInstCodeHostNameSuffix
                if (item.name == rbInstCodeHostNameSuffix) {
                    hostNameSuffix = item.value
                }
                // rbInstCodeApplication
                if (item.name == rbInstCodeApplication) {
                    application = item.value
                }
                // cpuArch
                if (item.name == rbInstCodeCpuArch) {
                    cpuArch = item.value
                }
                // memberId
                if (item.name.contains('memberId')) {
                    memberId = item.value
                }
            }
        }

        def systemTemplateId = ""
        //筛选系统模板
        systemTemplate.each { key, value ->
            if (value.cpuArch.contains(cpuArch) && value.os == os && value.serverType == serverType){
                systemTemplateId = key
            }
        }

        //获取物理机设备列表
        def pmGroupBy = getOracleRacPmGroupBy(memberId, serverType,os,cpuArch, params)
        params.logger.info("pmGroupBy" + JSON.toJSONString(pmGroupBy))
        //获取符合条件的物理机
        def resultPm = getOracleRacPmListByCondition(pmGroupBy,memberId,serverType,os,cpuArch, params)
        params.logger.info("resultPm" + JSON.toJSONString(resultPm))

        if (resultPm.size() <num) {
            throw new CommonBizException("当前可用物理机小于申请数量")
        }

        resultPm = resultPm.subList(0, num);

        //过滤出剩余IP数量最多的网段
        def networkId = getMaxUnusedIpNumNetwork(num, memberId, networkArea[networkAreaParam]);
        //根据过滤的网段Id分配IP
        def ipList = getIpByCidr(networkId, memberId, num, params,serviceAccountId)

        def bpStepList = []
        def pmIdList = [];
        for (int i = 0; i < num; i++) {
            def pmBatch = resultPm[i];
            pmIdList.add(pmBatch.ID);

            def oneBatch = [:]

            if (ipList.size() != 0) {//？？？该方法调用方法获取的值不变
                oneBatch.put('ips', [ipList[i].ip])//是否有下标越界问题
            }
            oneBatch.put('sn', pmBatch.SN)
            //脚本信息可以空
            oneBatch.put('hardwareTemplateId', hardwareTemplateId)
            //系统模板
            oneBatch.put('systemTemplateId', systemTemplateId)
            //固定 installType = pxe
            oneBatch.put('installType', installType)

            // osUsername 和 osPassword 暂时不用
            oneBatch.put('osUsername', 'test')
            oneBatch.put('osPassword', 'zaq1@WSX')

            //带外ip oob从物理机列表中获取
            if (StringUtils.isNotBlank(pmBatch.OOB_IP)){
                oneBatch.put('oobIp', pmBatch.OOB_IP)
                oneBatch.put('oobIpSource', "static")
                //根据IP查询网段信息
//                def cidrEntityId = getNetworkByIp(pmBatch.OOB_IP, memberId)
//                oneBatch.put('oobNetworkId', cidrEntityId)
            } else {
                oneBatch.put('oobIp', "")
                oneBatch.put('oobIpSource',  "")
                oneBatch.put('oobNetworkId', "")
            }

            def regionEntityId = getSysRegionById(pmBatch.REGION_ID, memberId, params)
            oneBatch.put('ipSource', "static")
            oneBatch.put('regionId', regionEntityId)
            def hostName = getPmName(i,hostNameSuffix,application)
            oneBatch.put('hostname', hostName)
            oneBatch.put('memberId', memberId)
            oneBatch.put('serviceAccountId', serviceAccountId)
            oneBatch.put('id', pmBatch.ID)
            oneBatch.put('entityId', pmBatch.ENTITY_ID)
            bpStepList.add(oneBatch)
        }

        def stepName = step.id + "_" + step.rbInstName + "_" + step.operation + '_multiInputParam';
        requestedItemInput.add(['name': stepName, 'value': JSONObject.toJSONString(bpStepList)])

        //处理完成之后更新设备状态
        updatePmStatus(pmIdList, memberId, params)
    }
}

/**
 * 过滤剩余IP最多的网段
 * @param
 * @return
 */
private String getMaxUnusedIpNumNetwork(num, memberId, networks) {
    def cmpResourcePoolManager = SpringContextUtil.getBean(CmpResourcePoolManager.class);
    def ipList = [];
    networks.each { it ->
        def query = new HashMap<String, String>()
        query.put('cidrId', it)

        def form = new CmpResourcePoolExcuteForm()
        form.setName('findUnsedIpNumByCidrId')
        form.setParams(query);
        form.setMemberId(memberId)
        params.logger.info(JSON.toJSONString(form))

        def resultList = cmpResourcePoolManager.executeMysqlDataPool(form)
        if (resultList != null) {
            def unusedIpNum = resultList[0].unusedIpNum
            //剩余的IP需要大于申请数量
            if (unusedIpNum >= num) {
                ipList.add(['networkId': it, "unusedIpNum": unusedIpNum])
            }
        }

    }

    if(ipList.size() == 0) {
        throw new CommonBizException("未找到可用的网段，申请数量：" + num);
    }

    ipList.sort{ a, b ->
        if (a.unusedIpNum < b.unusedIpNum) {
            return 1
        } else if(a.unusedIpNum == b.unusedIpNum){
            return 0
        } else {
            return -1
        }
    }

    return ipList[0].networkId;
}

/**
 * 处理完成之后更新设备状态
 * @param clusterId
 * @return
 */
private void updatePmStatus(pmIdList, memberId, params) {
    params.logger.info("更改pm的状态：" + pmIdList);
    def cloudBootManager = SpringContextUtil.getBean(CloudBootManager.class);
    def bootPmUpdateAssignedForm = new BootPmUpdateAssignedForm()
    bootPmUpdateAssignedForm.setIds(pmIdList)
    bootPmUpdateAssignedForm.setAssigned(true)
    bootPmUpdateAssignedForm.setMemberId(memberId)
    bootPmUpdateAssignedForm.setLastModifiedBy("1")
    cloudBootManager.updatePmAssigned(bootPmUpdateAssignedForm)
}

/**
 * 获取物理机列表
 * @param clusterId
 * @return
 */
private static Object getOracleRacPmGroupBy(memberId, serverType, os,cpuArch, params) {
    def cmpResourcePoolManager = SpringContextUtil.getBean(CmpResourcePoolManager.class);
    def query = new HashMap<String, String>();
    query.put("usedFor",serverType)
    query.put("osName",os)
    query.put("cpuArch",cpuArch)
    def form = new CmpResourcePoolExcuteForm();
    form.setName('findMysqlPmGrouBy')
    form.setParams(query)
    form.setMemberId(memberId)

    params.logger.info("查询groupby数据为：" + form);

    def resultList = cmpResourcePoolManager.executeMysqlDataPool(form)
    def pmInfo = [:]
    for (int i = 0; i < resultList.size(); i++) {
        //if (resultList[i].num >= 2) {
        if (resultList[i].num >= 1) {
            pmInfo = resultList[i]
            break
        }
    }
    return pmInfo
}


/**
 * 获取物理机列表
 * @param clusterId
 * @return
 */
private static List<Object> getOracleRacPmListByCondition(pmGroupBy,memberId,serverType,os,cpuArch, params) {
    def cmpResourcePoolManager = SpringContextUtil.getBean(CmpResourcePoolManager.class)
    def query = new HashMap<String, String>()
    query.put("usedFor",serverType)
    query.put("company", String.valueOf(pmGroupBy.MANUFACTURER))
    query.put("model", String.valueOf(pmGroupBy.MODEL_ID))
    query.put("cpu", String.valueOf(pmGroupBy.CPU_CORE_COUNT))
    query.put("ram", String.valueOf(pmGroupBy.RAM))
    query.put("diskSpace", String.valueOf(pmGroupBy.DISK_SPACE))
    query.put("computerRoomId", String.valueOf(pmGroupBy.COMPUTER_ROOM_ID))
    query.put("osName",os)
    query.put("cpuArch",cpuArch)

    def form = new CmpResourcePoolExcuteForm()
    form.setName("findMysqlPmListByCondition")
    form.setParams(query);
    form.setMemberId(memberId)

    params.logger.info("查询condition数据为：" + form);
    def resultList = cmpResourcePoolManager.executeMysqlDataPool(form)
    return resultList
}

/**
 * 根据设备id获取nic
 * @param cidrId
 * @param params
 * @return
 */
private static List<Object> getNicByServerId(serverId, memberId) {
    def cmpResourcePoolManager = SpringContextUtil.getBean(CmpResourcePoolManager.class);
    def query = new HashMap<String, String>()
    query.put('serverId', String.valueOf(serverId));

    def form = new CmpResourcePoolExcuteForm()
    form.setName('findPhysicsNicByServerId')
    form.setParams(query)
    form.setMemberId(memberId)

    def resultList = cmpResourcePoolManager.executeMysqlDataPool(form)
    return resultList
}

/**
 * 根据网段分配IP
 * @param cidrId
 * @param params
 * @return
 */
private static List<Object> getIpByCidr(cidrId, memberId, num, params,serviceAccountId) {
    def cidrBlockManager = SpringContextUtil.getBean(CidrBlockManager.class)
    def networkId = Long.parseLong(cidrId)
    def member = Long.parseLong(memberId)
    if (params.assignIp == null || params.assignIP) {
        def ips = cidrBlockManager.assignIp(networkId, num, member,serviceAccountId);
        return ips
    } else {
        params.logger.info("调试中，不分配IP")
        return []
    }
}

/**
 * 根据IP获取网段entityId
 * @param ip
 * @param params
 * @return
 */
private static String getNetworkByIp(ip, memberId) {
    def cmpResourcePoolManager = SpringContextUtil.getBean(CmpResourcePoolManager.class);
    def query = new HashMap<String, String>()
    query.put('ip', ip);

    def form = new CmpResourcePoolExcuteForm()
    form.setName('findCidrByIp')
    form.setParams(query)
    form.setMemberId(memberId)

    def resultList = cmpResourcePoolManager.executeMysqlDataPool(form)
    return resultList[0].etityId
}

/**
 * 根据Id查询region详情
 * @param id
 * @return
 */
private static String getSysRegionById(regionId, memberId, params) {
    def cmpResourcePoolManager = SpringContextUtil.getBean(CmpResourcePoolManager.class);
    def query = new HashMap<String, String>()
    query.put('regionId', String.valueOf(regionId));

    def form = new CmpResourcePoolExcuteForm()
    form.setName('findSysRegionById')
    form.setParams(query)
    form.setMemberId(memberId)

    params.logger.info('查询region参数：' + form);

    def resultList = cmpResourcePoolManager.executeMysqlDataPool(form)
    return resultList[0].ENTITY_ID
}

/**
 * 构建物理机名称hostName
 * @param i
 * @return
 */
private static String getPmName(i, nameSuffix, application) {
    def namePrefix = 'HQxPSL-'

    //增加应用简称，并将服务器类型默认设置A
    if (StringUtils.isNotBlank(application)) {
        namePrefix = namePrefix + application + "-"
    }

    def suffix = nameSuffix + i
    def hostName = namePrefix + String.valueOf(suffix)
    return hostName
}

return ["parameters": params.parameters]