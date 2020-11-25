package com.idcos.cmp.core.biz.groovy

import com.idcos.cmp.core.biz.manager.IpPoolManager
import com.idcos.cmp.core.biz.util.SpringContextUtil

def requestedItermInput = params.parameters.requestedItermInput
def memberId = 0L
def cidrBlockID = 0L
def ip = ""
requestedItermInput.each{item->
    if (item.name.contains("memberId")){
        memberId = item.value
    }
    if (item.name.contains("cidrBlockID")){
        cidrBlockID = item.value
    }
    if (item.name.contains("ip")){
        ip = item.value
    }
}
//判断是否是蓝图数据
if (params.parameters.catalogItem.source == 'blueprint') {
    def ipPoolManager = SpringContextUtil.getBean(IpPoolManager.class)
    ipPoolManager.releaseIp(memberId,cidrBlockID,ip)
}

return ["parameters": params.parameters]