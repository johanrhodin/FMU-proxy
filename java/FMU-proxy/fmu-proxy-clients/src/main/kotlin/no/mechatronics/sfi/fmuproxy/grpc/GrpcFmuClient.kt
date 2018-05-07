/*
 * The MIT License
 *
 * Copyright 2017-2018 Norwegian University of Technology (NTNU)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING  FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package no.mechatronics.sfi.fmuproxy.grpc


import com.google.protobuf.Empty
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import no.mechatronics.sfi.fmi4j.common.*
import no.mechatronics.sfi.fmi4j.modeldescription.CommonModelDescription
import no.mechatronics.sfi.fmuproxy.IntegratorSettings
import no.mechatronics.sfi.fmuproxy.RpcFmuClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val EMPTY = Empty.getDefaultInstance()


/**
 * @author Lars Ivar Hatledal
 */
class GrpcFmuClient(
        host: String,
        port: Int
): RpcFmuClient() {

    private val channel: ManagedChannel = ManagedChannelBuilder
            .forAddress(host, port)
            .usePlaintext()
            .directExecutor()
            .build()

    private val blockingStub: FmuServiceGrpc.FmuServiceBlockingStub
            = FmuServiceGrpc.newBlockingStub(channel)

    override val modelDescription: CommonModelDescription by lazy {
        blockingStub.getModelDescription(EMPTY).convert()
    }

    override val modelDescriptionXml: String by lazy {
        blockingStub.getModelDescriptionXml(EMPTY).value
    }

    override fun getCurrentTime(fmuId: Int): Double {
        return blockingStub.getCurrentTime(fmuId.asProtoUInt()).value
    }

    override fun isTerminated(fmuId: Int): Boolean {
        return blockingStub.isTerminated(fmuId.asProtoUInt()).value
    }

    override fun init(fmuId: Int, start: Double, stop: Double): FmiStatus {
        return Proto.InitRequest.newBuilder()
                .setFmuId(fmuId)
                .setStart(start)
                .setStop(stop)
                .build().let {
                    blockingStub.init(it).convert()
                }
    }

    override fun step(fmuId: Int, stepSize: Double): FmiStatus {
        return Proto.StepRequest.newBuilder()
                .setFmuId(fmuId)
                .setStepSize(stepSize)
                .build().let {
                    blockingStub.step(it).convert()
                }
    }

    override fun reset(fmuId: Int): FmiStatus {
        return blockingStub.reset(fmuId.asProtoUInt()).convert()
    }

    override fun terminate(fmuId: Int): FmiStatus {
        return blockingStub.terminate(fmuId.asProtoUInt()).convert()
    }

    private fun getReadRequest(fmuId: Int, vr: Int): Proto.ReadRequest {
        return Proto.ReadRequest.newBuilder()
                .setFmuId(fmuId)
                .setValueReference(vr)
                .build()
    }

    private fun getReadRequest(fmuId: Int, vr: List<Int>): Proto.BulkReadRequest {
        return Proto.BulkReadRequest.newBuilder()
                .setFmuId(fmuId)
                .addAllValueReferences(vr)
                .build()
    }

    override fun readInteger(fmuId: Int, vr: Int): FmuIntegerRead {
        return blockingStub.readInteger(getReadRequest(fmuId, vr)).convert()
    }

    override fun bulkReadInteger(fmuId: Int, vr: List<Int>): FmuIntegerArrayRead {
        return blockingStub.bulkReadInteger(getReadRequest(fmuId, vr)).convert()
    }

    override fun readReal(fmuId: Int, vr: Int): FmuRealRead {
        return blockingStub.readReal(getReadRequest(fmuId, vr)).convert()
    }

    override fun bulkReadReal(fmuId: Int, vr: List<Int>): FmuRealArrayRead {
        return blockingStub.bulkReadReal(getReadRequest(fmuId, vr)).convert()
    }

    override fun readString(fmuId: Int, vr: Int): FmuStringRead {
        return blockingStub.readString(getReadRequest(fmuId, vr)).convert()
    }

    override fun bulkReadString(fmuId: Int, vr: List<Int>): FmuStringArrayRead {
        return blockingStub.bulkReadString(getReadRequest(fmuId, vr)).convert()
    }

    override fun readBoolean(fmuId: Int, vr: Int): FmuBooleanRead {
        return blockingStub.readBoolean(getReadRequest(fmuId, vr)).convert()
    }

    override fun bulkReadBoolean(fmuId: Int, vr: List<Int>): FmuBooleanArrayRead {
        return blockingStub.bulkReadBoolean(getReadRequest(fmuId, vr)).convert()
    }

    override fun writeInteger(fmuId: Int, vr: ValueReference, value: Int): FmiStatus {
        return Proto.WriteIntRequest.newBuilder()
                .setFmuId(fmuId)
                .setValueReference(vr)
                .setValue(value)
                .build().let {
                    blockingStub.writeInteger(it).convert()
                }
    }

    override fun bulkWriteInteger(fmuId: Int, vr: List<Int>, value: List<Int>): FmiStatus {
        return Proto.BulkWriteIntRequest.newBuilder()
                .setFmuId(fmuId)
                .addAllValueReferences(vr)
                .addAllValues(value)
                .build().let {
                    blockingStub.bulkWriteInteger(it).convert()
                }
    }

    override fun writeReal(fmuId: Int, vr: ValueReference, value: Real): FmiStatus {
        return Proto.WriteRealRequest.newBuilder()
                .setFmuId(fmuId)
                .setValueReference(vr)
                .setValue(value)
                .build().let {
                    blockingStub.writeReal(it).convert()
                }
    }

    override fun bulkWriteReal(fmuId: Int, vr: List<Int>, value: List<Real>): FmiStatus {
        return Proto.BulkWriteRealRequest.newBuilder()
                .setFmuId(fmuId)
                .addAllValueReferences(vr)
                .addAllValues(value)
                .build().let {
                    blockingStub.bulkWriteReal(it).convert()
                }
    }

    override fun writeString(fmuId: Int, vr: ValueReference, value: String): FmiStatus {
        return Proto.WriteStrRequest.newBuilder()
                .setFmuId(fmuId)
                .setValueReference(vr)
                .setValue(value)
                .build().let {
                    blockingStub.writeString(it).convert()
                }
    }

    override fun bulkWriteString(fmuId: Int, vr: List<Int>, value: List<String>): FmiStatus {
        return Proto.BulkWriteStrRequest.newBuilder()
                .setFmuId(fmuId)
                .addAllValueReferences(vr)
                .addAllValues(value)
                .build().let {
                    blockingStub.bulkWriteString(it).convert()
                }
    }

    override fun writeBoolean(fmuId: Int, vr: ValueReference, value: Boolean): FmiStatus {
        return Proto.WriteBoolRequest.newBuilder()
                .setFmuId(fmuId)
                .setValueReference(vr)
                .setValue(value)
                .build().let {
                    blockingStub.writeBoolean(it).convert()
                }
    }

    override fun bulkWriteBoolean(fmuId: Int, vr: List<Int>, value: List<Boolean>): FmiStatus {
        return Proto.BulkWriteBoolRequest.newBuilder()
                .setFmuId(fmuId)
                .addAllValueReferences(vr)
                .addAllValues(value)
                .build().let {
                    blockingStub.bulkWriteBoolean(it).convert()
                }
    }

    override fun createInstanceFromCS(): Int {
        return blockingStub.createInstanceFromCS(EMPTY).value
    }

    override fun createInstanceFromME(integrator: IntegratorSettings): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
        super.close()
        channel.shutdownNow()
    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(GrpcFmuClient::class.java)
    }


//    inner class FmuInstance internal constructor(
//            private val fmuId: Int
//    ) : Closeable {
//
//        private val modelRef = Proto.UInt.newBuilder()
//                .setValue(fmuId).build()
//
//        val currentTime: Double
//            get() = blockingStub.getCurrentTime(modelRef).value
//
//        @JvmOverloads
//        fun init(start: Double = 0.0, stop: Double = -1.0): Proto.Status {
//            return blockingStub.init(
//                    Proto.InitRequest.newBuilder()
//                            .setFmuId(fmuId)
//                            .setStart(start)
//                            .setStop(stop)
//                            .build())
//        }
//
//        fun step(stepSize: Double): Proto.Status = blockingStub.step(Proto.StepRequest.newBuilder()
//                .setFmuId(fmuId)
//                .setStepSize(stepSize)
//                .build())
//
//        fun terminate(): Proto.Status {
//            return blockingStub.terminate(modelRef)
//        }
//
//        override fun close() {
//            terminate()
//        }
//
//        fun reset(): Proto.Status {
//            return blockingStub.reset(modelRef)
//        }
//
//        fun getValueReference(variableName: String): Int {
//             return modelDescription.modelVariablesList
//                    .firstOrNull { it.name == variableName }?.valueReference
//                    ?: throw IllegalArgumentException("No such variable: $variableName")
//        }
//
//        fun read(valueReference: Int) = SingleRead(valueReference)
//        fun read(valueReferences: IntArray) = BulkRead(valueReferences)
//
//        fun read(variableName: String) = SingleRead(variableName)
//        fun read(variableNames: Array<String>) = BulkRead(variableNames)
//
//        fun write(valueReference: Int) = SingleWrite(valueReference)
//        fun write(valueReferences: IntArray) = BulkRead(valueReferences)
//
//        inner class SingleRead(
//                private val valueReference: Int
//        ) {
//
//            constructor(variableName: String): this(getValueReference(variableName))
//
//            fun asInt(): Proto.IntRead {
//                return blockingStub.readInteger(Proto.ReadRequest.newBuilder()
//                        .setFmuId(fmuId)
//                        .setValueReference(valueReference)
//                        .build())
//            }
//
//            fun asReal(): Proto.RealRead {
//                return blockingStub.readReal(Proto.ReadRequest.newBuilder()
//                        .setFmuId(fmuId)
//                        .setValueReference(valueReference)
//                        .build())
//            }
//
//            fun asString(): Proto.StrRead {
//                return blockingStub.readString(Proto.ReadRequest.newBuilder()
//                        .setFmuId(fmuId)
//                        .setValueReference(valueReference)
//                        .build())
//            }
//
//            fun asBoolean(): Proto.BoolRead {
//                return blockingStub.readBoolean(Proto.ReadRequest.newBuilder()
//                        .setFmuId(fmuId)
//                        .setValueReference(valueReference)
//                        .build())
//            }
//
//        }
//
//        inner class BulkRead(
//                private val valueReferences: IntArray
//        ) {
//
//            constructor(variableNames: Array<String>) : this(variableNames.map { getValueReference(it) }.toIntArray())
//
//
//            fun readInt(): List<Int> {
//                val builder = Proto.BulkReadRequest.newBuilder().setFmuId(fmuId)
//                valueReferences.forEachIndexed{ i, v -> builder.setValueReferences(i, v)}
//                return blockingStub.bulkReadInteger(builder.build()).valuesList
//            }
//
//            fun readReal(): List<Double> {
//                val builder = Proto.BulkReadRequest.newBuilder().setFmuId(fmuId)
//                valueReferences.forEachIndexed{ i, v -> builder.setValueReferences(i, v)}
//                return blockingStub.bulkReadReal(builder.build()).valuesList
//            }
//            fun readString(): List<String> {
//                val builder = Proto.BulkReadRequest.newBuilder().setFmuId(fmuId)
//                valueReferences.forEachIndexed{ i, v -> builder.setValueReferences(i, v)}
//                return blockingStub.bulkReadString(builder.build()).valuesList
//            }
//
//            fun readBoolean(): List<Boolean> {
//                val builder = Proto.BulkReadRequest.newBuilder().setFmuId(fmuId)
//                valueReferences.forEachIndexed{ i, v -> builder.setValueReferences(i, v)}
//                return blockingStub.bulkReadBoolean(builder.build()).valuesList
//            }
//
//        }
//
//        inner class SingleWrite(
//                private val valueReference: Int
//        ) {
//
//            fun with(value: Int): Proto.Status {
//                return blockingStub.writeInteger(Proto.WriteIntRequest.newBuilder()
//                        .setFmuId(fmuId)
//                        .setValueReference(valueReference)
//                        .setValue(value)
//                        .build())
//            }
//
//            fun with(value: Double): Proto.Status {
//                return blockingStub.writeReal(Proto.WriteRealRequest.newBuilder()
//                        .setFmuId(fmuId)
//                        .setValueReference(valueReference)
//                        .setValue(value)
//                        .build())
//            }
//
//            fun with(value: String): Proto.Status {
//                return blockingStub.writeString(Proto.WriteStrRequest.newBuilder()
//                        .setFmuId(fmuId)
//                        .setValueReference(valueReference)
//                        .setValue(value)
//                        .build())
//            }
//
//            fun with(value: Boolean): Proto.Status {
//                return blockingStub.writeBoolean(Proto.WriteBoolRequest.newBuilder()
//                        .setFmuId(fmuId)
//                        .setValueReference(valueReference)
//                        .setValue(value)
//                        .build())
//            }
//
//        }
//
//        inner class BulkWrite(
//                private val valueReferences: IntArray
//        ) {
//            constructor(variableNames: Array<String>) : this(variableNames.map { getValueReference(it) }.toIntArray())
//
//            fun with(values: IntArray): Proto.Status {
//                val builder = Proto.BulkWriteIntRequest.newBuilder().setFmuId(fmuId)
//                values.forEachIndexed{i,v -> builder.setValues(i, v)}
//                valueReferences.forEachIndexed { i, v ->  builder.setValueReferences(i, v)}
//                return blockingStub.bulkWriteInteger(builder.build())
//            }
//
//            fun with(values: DoubleArray): Proto.Status {
//                val builder = Proto.BulkWriteRealRequest.newBuilder().setFmuId(fmuId)
//                values.forEachIndexed{i,v -> builder.setValues(i, v)}
//                valueReferences.forEachIndexed { i, v ->  builder.setValueReferences(i, v)}
//                return blockingStub.bulkWriteReal(builder.build())
//            }
//
//            fun with(values: Array<String>): Proto.Status {
//                val builder = Proto.BulkWriteStrRequest.newBuilder().setFmuId(fmuId)
//                values.forEachIndexed{i,v -> builder.setValues(i, v)}
//                valueReferences.forEachIndexed { i, v ->  builder.setValueReferences(i, v)}
//                return blockingStub.bulkWriteString(builder.build())
//            }
//
//            fun with(values: BooleanArray): Proto.Status {
//                val builder = Proto.BulkWriteBoolRequest.newBuilder().setFmuId(fmuId)
//                values.forEachIndexed{i,v -> builder.setValues(i, v)}
//                valueReferences.forEachIndexed { i, v ->  builder.setValueReferences(i, v)}
//                return blockingStub.bulkWriteBoolean(builder.build())
//            }
//
//        }
//
//    }


}

