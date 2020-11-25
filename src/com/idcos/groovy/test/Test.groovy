package com.idcos.groovy.test

def updateSpec_spec = '1C21G'
def cpu = updateSpec_spec.split('C')[0] as int
def memInMB = updateSpec_spec.split('C')[1].split('G')[0] as int
println cpu
println memInMB