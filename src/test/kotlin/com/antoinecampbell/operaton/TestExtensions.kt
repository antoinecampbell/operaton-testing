package com.antoinecampbell.operaton

import org.operaton.bpm.engine.externaltask.ExternalTask
import org.operaton.bpm.engine.externaltask.LockedExternalTask
import org.operaton.bpm.engine.test.assertions.bpmn.BpmnAwareTests.DEFAULT_WORKER_EXTERNAL_TASK
import org.operaton.bpm.engine.test.assertions.bpmn.BpmnAwareTests.externalTaskService
import org.operaton.bpm.engine.test.assertions.bpmn.BpmnAwareTests.fetchAndLock
import org.operaton.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService
import org.operaton.bpm.engine.test.assertions.bpmn.ProcessInstanceAssert


fun ProcessInstanceAssert.hasIncident(): ProcessInstanceAssert {
    runtimeService().createIncidentQuery()
        .processInstanceId(this.actual.id)
        .list().isNotEmpty()
    return this
}

fun fetchAndLockExternalTask(
    topic: String
): LockedExternalTask = fetchAndLock(
    /* topic = */ topic,
    /* workerId = */ DEFAULT_WORKER_EXTERNAL_TASK,
    /* maxResults = */ 1
).single()

fun failExternalTask(
    task: ExternalTask,
    errorMessage: String = "Simulated failure"
) {
    val task = fetchAndLockExternalTask(task.topicName)
    externalTaskService().handleFailure(
        /* externalTaskId = */ task.id,
        /* workerId = */ DEFAULT_WORKER_EXTERNAL_TASK,
        /* errorMessage = */ errorMessage,
        /* retries = */ 0,
        /* retryTimeout = */ 0
    )
}
