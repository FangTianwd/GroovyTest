package groovytest

import com.alibaba.fastjson.JSONObject
import com.idcos.cmp.core.appmgr.manager.AppDuManager;
import com.idcos.cmp.core.appmgr.manager.AppInstanceManager
import com.idcos.cmp.core.appmgr.vo.AppDuVO
import com.idcos.cmp.core.appmgr.vo.AppInstanceVO
import com.idcos.cmp.core.biz.common.template.CommonBizException
import com.idcos.cmp.core.biz.manager.CidrBlockManager
import com.idcos.cmp.core.biz.manager.FusionCloudNetworkSubnetManager
import com.idcos.cmp.core.biz.resourcepool.CmpResourcePoolManager
import com.idcos.cmp.core.biz.resourcepool.form.CmpResourcePoolExcuteForm
import com.idcos.cmp.core.biz.util.SpringContextUtil
import org.apache.commons.lang3.StringUtils
import org.springframework.util.Base64Utils;
import java.util.Random;

//定义输出结构
def result = [:];

//定义返回数据中的data部分
def resultData = [];
//定义返回数据中的displayData部分
def displayResultData = [];

//先从虚拟机主键Id
def requestedItemInput = params.parameters.requestedItemInput;
def memberId = 0;
def sourceIp = '';
requestedItemInput.each{ it ->
    //将原始数据放入返回对象中
    resultData.add(it);
    displayResultData.add(it);

    if (it.name.contains("memberId")) {
        memberId = it.value;
    }
    if (it.name.contains('ip')) {
        sourceIp = it.value;
    }
}

def sourceIpList = sourceIp.split(',');

//如果是蓝图数据
if (params.parameters.catalogItem.source == 'blueprint') {
    def steps = params.parameters.catalogItem.metadata.steps;

    def baseNetworkInfo;
    def stepOneList = [];
    steps.eachWithIndex { step, index ->

        if (index == 0) {
            def bpStepList = [];

            //用于前端展示的参数
            def displayList = [];
            for (int i = 0; i < sourceIpList.length; i++) {
                def ip = sourceIpList[i];
                //获取基础信息
                def vmInfo = getVmDetail(ip, memberId, params);
                if (vmInfo == null) {
                    throw new CommonBizException("IP地址：" + ip + ",未查询主机信息");
                }

                def oneBatch = [:];

                oneBatch.put('projectId', vmInfo.LDC_ENTITY_ID);
                oneBatch.put('instanceId', vmInfo.ENTITY_ID);
                oneBatch.put('memberId', memberId);
                oneBatch.put('serviceAccountId', vmInfo.SERVICE_ACCOUNT_ID)
                oneBatch.put('cloudAccountId', vmInfo.CLOUD_ACCOUNT_ID);
                oneBatch.put('ldcId', vmInfo.LDC_ID);
                oneBatch.put('id', vmInfo.ID);
                oneBatch.put('instanceName', vmInfo.NAME)

                bpStepList.add(oneBatch);
                stepOneList.add(oneBatch);

                //构建中文显示名称
                def displayBatch = [:];
                displayBatch.put('name', ['label': '虚拟机名称', 'value': vmInfo.NAME]);
                displayBatch.put('IP', ['label': 'IP地址', 'value': vmInfo.IP]);

                displayList.add(displayBatch);
            }

            //将创建主机的参数与显示参数放入name中，流程与前端根据name来创建主机或显示
            def name = step.id + "_" + step.rbInstName + "_" + step.operation + '_multiInputParam';
            requestedItemInput.add(['name': name, 'value': JSONObject.toJSONString(bpStepList)])

            resultData.add(JSONObject.parseObject(JSONObject.toJSONString(['name': name, 'value': JSONObject.toJSONString(bpStepList)])));
            displayResultData.add(JSONObject.parseObject(JSONObject.toJSONString(['name': name, 'value': JSONObject.toJSONString(displayList)])));
        }

        if (index == 1) {
            params.logger.info("第二个步骤：" + stepOneList);
            def bpStepList = [];
            def displayList = [];
            stepOneList.each { it ->
                def vmDeviceId = it.id;

                def vmVolumeList = getVmVolumeList(String.valueOf(vmDeviceId), memberId, params);
                if (vmVolumeList.size() == 0) {
                    params.logger.info("主机[" + it.instanceName + "]没有数据盘");
                    return true
                }


                vmVolumeList.each { volume ->
                    def oneBatch = [:];

                    oneBatch.put('diskEntityId', volume.ENTITY_ID);
                    oneBatch.put('memberId', memberId);
                    oneBatch.put('serviceAccountId', it.serviceAccountId)
                    oneBatch.put('cloudAccountId', it.cloudAccountId);
                    oneBatch.put('ldcId', it.ldcId);

                    bpStepList.add(oneBatch);

                    //构建中文显示名称
                    def displayBatch = [:];
                    displayBatch.put('name', ['label': '磁盘名称', 'value': volume.NAME]);
                    displayBatch.put('entityId', ['label': '磁盘标识', 'value': volume.ENTITY_ID]);

                    displayList.add(displayBatch);
                }
            }

            //将创建主机的参数与显示参数放入name中，流程与前端根据name来创建主机或显示
            def name = step.id + "_" + step.rbInstName + "_" + step.operation + '_multiInputParam';
            requestedItemInput.add(['name': name, 'value': JSONObject.toJSONString(bpStepList)])

            resultData.add(JSONObject.parseObject(JSONObject.toJSONString(['name': name, 'value': JSONObject.toJSONString(bpStepList)])));
            displayResultData.add(JSONObject.parseObject(JSONObject.toJSONString(['name': name, 'value': JSONObject.toJSONString(displayList)])));
        }
    }

    //构建返回对象
    result.put('data', JSONObject.parseArray(JSONObject.toJSONString(resultData)))
    result.put('displayData', JSONObject.parseArray(JSONObject.toJSONString(displayResultData)))
}

/**
 * 获取虚拟机的磁盘
 *
 */
private static List getVmVolumeList(vmDeviceId, memberId, params) {
    def cmpResourcePoolManager = SpringContextUtil.getBean(CmpResourcePoolManager.class);
    def query = new HashMap<String, String>();
    query.put('id', vmDeviceId);

    def form = new CmpResourcePoolExcuteForm();
    form.setName('findVolumeByID');
    form.setMemberId(memberId)
    form.setParams(query)

    def resultList = cmpResourcePoolManager.executeMysqlDataPool(form)
    params.logger.info("查询到的卷详情数据为：" + resultList);


    def list = [];
    resultList.each { it ->
        //去除系统盘
        if (it.DEVICE.contains('sda')) {
            return true;
        }
        params.logger.info("加入到列表中：" + it);
        list.add(it)
    }
    return list;
}


/**
 * 根据虚拟机IP查询详情信息
 */
private Object getVmDetail(sourceIp, memberId, params) {
    def cmpResourcePoolManager = SpringContextUtil.getBean(CmpResourcePoolManager.class);
    def query = new HashMap<String, String>();
    query.put('devIp', sourceIp);
    query.put('ldcType', 'fusioncloud');

    def form = new CmpResourcePoolExcuteForm();
    form.setName('findVmInfoByIpAndLdcType');
    form.setMemberId(String.valueOf(memberId))
    form.setParams(query);

    def resultList = cmpResourcePoolManager.executeMysqlDataPool(form)
    params.logger.info("查询到的虚拟机详情数据为：" + resultList);


    def vmInfo;
    resultList.each { it ->
        if (it.IP.startsWith('[')) {
            def ips = JSONObject.parseArray(it.IP);
            ips.each { ip ->
                if (sourceIp == ip) {
                    vmInfo = it
                }
            }
        } else {
            if (it.IP == sourceIp) {
                params.logger.info('比对成功')
                vmInfo = it;
            }
        }
    }

    return vmInfo
}

return ["parameters": ['requestedItemInput': result]]