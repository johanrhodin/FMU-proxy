package no.mechatronics.sfi.fmuproxy.thrift

import no.mechatronics.sfi.fmi4j.fmu.Fmu
import no.mechatronics.sfi.fmi4j.modeldescription.CommonModelDescription
import no.mechatronics.sfi.fmuproxy.TestUtils
import no.mechatronics.sfi.fmuproxy.runInstance
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

@EnabledOnOs(OS.WINDOWS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnabledIfEnvironmentVariable(named = "TEST_FMUs", matches = ".*")
class TestThriftBouncing {

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(TestThriftBouncing::class.java)
    }

    private val fmu: Fmu
    private val server: ThriftFmuServer
    private val client: ThriftFmuClient
    private val modelDescription: CommonModelDescription

    init {

        fmu = Fmu.from(File(TestUtils.getTEST_FMUs(),
                "FMI_2.0/CoSimulation/win64/FMUSDK/2.0.4/BouncingBall/bouncingBall.fmu"))
        modelDescription = fmu.modelDescription

        server = ThriftFmuServer(fmu)
        val port = server.start()

        client = ThriftFmuClient("localhost", port)
    }

    @AfterAll
    fun tearDown() {
        client.close()
        server.stop()
        fmu.close()
    }

    @Test
    fun testGuid() {
        val guid = client.modelDescription.guid.also { LOG.info("guid=$it") }
        Assertions.assertEquals(modelDescription.guid, guid)
    }

    @Test
    fun testModelName() {
        val modelName = client.modelDescription.modelName.also { LOG.info("modelName=$it") }
        Assertions.assertEquals(modelDescription.modelName, modelName)
    }

    @Test
    fun testInstance() {

        client.newInstance().use { instance ->

            val h = client.modelDescription.modelVariables
                    .getByName("h").asRealVariable()

            val dt = 1.0/100
            val stop = 100.0
            runInstance(instance, dt, stop, {
                h.read()
            }).also {
                LOG.info("Duration: ${it}ms")
            }

        }

    }


}