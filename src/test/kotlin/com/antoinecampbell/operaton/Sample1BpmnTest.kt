package com.antoinecampbell.operaton

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.operaton.bpm.engine.test.Deployment
import org.operaton.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat
import org.operaton.bpm.engine.test.assertions.bpmn.BpmnAwareTests.complete
import org.operaton.bpm.engine.test.assertions.bpmn.BpmnAwareTests.externalTask
import org.operaton.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService
import org.operaton.bpm.engine.test.junit5.ProcessEngineExtension

@Deployment(resources = ["diagrams/sample1.bpmn"])
@ExtendWith(ProcessEngineExtension::class)
class Sample1BpmnTest {

    @Test
    fun `should start and wait at first task`() {
        val processInstance = runtimeService()
            .createProcessInstanceByKey("sample1")
            .execute()

        assertThat(processInstance).isStarted().isActive()
        assertThat(processInstance).isStarted().isWaitingAt("task1")
    }

    @Test
    fun `should complete first task and end process`() {
        val processInstance = runtimeService()
            .createProcessInstanceByKey("sample1")
            .execute()
        // Complete task 1
        complete(externalTask("task1", processInstance))

        assertThat(processInstance).isEnded()
    }

    @Test
    fun `should fail first task and raise incident`() {
        val processInstance = runtimeService()
            .createProcessInstanceByKey("sample1")
            .execute()
        // Fail task 1
        failExternalTask(externalTask("task1", processInstance))

        assertThat(processInstance).hasIncident()
        assertThat(processInstance).isNotEnded()
    }
}
