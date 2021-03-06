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

import no.mechatronics.sfi.fmi4j.common.*
import no.mechatronics.sfi.fmi4j.modeldescription.CommonModelDescription
import no.mechatronics.sfi.fmi4j.modeldescription.misc.LogCategories
import no.mechatronics.sfi.fmi4j.modeldescription.misc.DefaultExperiment
import no.mechatronics.sfi.fmi4j.modeldescription.misc.VariableNamingConvention
import no.mechatronics.sfi.fmi4j.modeldescription.structure.DependenciesKind
import no.mechatronics.sfi.fmi4j.modeldescription.structure.ModelStructure
import no.mechatronics.sfi.fmi4j.modeldescription.structure.Unknown
import no.mechatronics.sfi.fmi4j.modeldescription.variables.*
import no.mechatronics.sfi.fmuproxy.Solver


internal fun Proto.StatusResponse.convert(): FmiStatus {
    return status.convert()
}

internal fun Proto.Status.convert(): FmiStatus {
    return when(this) {
        Proto.Status.OK_STATUS -> FmiStatus.OK
        Proto.Status.DISCARD_STATUS -> FmiStatus.Discard
        Proto.Status.ERROR_STATUS -> FmiStatus.Error
        Proto.Status.WARNING_STATUS -> FmiStatus.Warning
        Proto.Status.PENDING_STATUS -> FmiStatus.Pending
        Proto.Status.FATAL_STATUS -> FmiStatus.Fatal
        Proto.Status.UNRECOGNIZED -> throw AssertionError()
    }
}

internal fun Proto.Causality.convert(): Causality {
    return when(this) {
        Proto.Causality.CALCULATED_PARAMETER_CAUSALITY -> Causality.CALCULATED_PARAMETER
        Proto.Causality.INDEPENDENT_CAUSALITY -> Causality.INDEPENDENT
        Proto.Causality.INPUT_CAUSALITY -> Causality.INPUT
        Proto.Causality.LOCAL_CAUSALITY -> Causality.LOCAL
        Proto.Causality.OUTPUT_CAUSALITY -> Causality.OUTPUT
        Proto.Causality.PARAMETER_CAUSALITY -> Causality.PARAMETER
        Proto.Causality.UNRECOGNIZED -> throw AssertionError()
    }
}

internal fun Proto.Variability.convert(): Variability {
    return when(this) {
        Proto.Variability.CONSTANT_VARIABILITY -> Variability.CONSTANT
        Proto.Variability.CONTINUOUS_VARIABILITY -> Variability.CONTINUOUS
        Proto.Variability.DISCRETE_VARIABILITY -> Variability.DISCRETE
        Proto.Variability.FIXED_VARIABILITY -> Variability.FIXED
        Proto.Variability.TUNABLE_VARIABILITY -> Variability.TUNABLE
        Proto.Variability.UNRECOGNIZED -> throw AssertionError()
    }
}

internal fun Proto.Initial.convert(): Initial {
    return when(this) {
        Proto.Initial.APPROX_INITIAL -> Initial.APPROX
        Proto.Initial.CALCULATED_INITIAL -> Initial.CALCULATED
        Proto.Initial.EXACT_INITIAL -> Initial.EXACT
        Proto.Initial.UNRECOGNIZED -> throw AssertionError()
    }
}

internal fun Proto.DependenciesKind.convert(): DependenciesKind {
    return when(this) {
        Proto.DependenciesKind.CONSTANT_KIND -> DependenciesKind.CONSTANT
        Proto.DependenciesKind.DEPENDENT_KIND -> DependenciesKind.DEPENDENT
        Proto.DependenciesKind.DISCRETE_KIND -> DependenciesKind.DISCRETE
        Proto.DependenciesKind.TUNABLE_KIND -> DependenciesKind.TUNABLE
        Proto.DependenciesKind.UNRECOGNIZED -> throw AssertionError()
    }
}

internal fun Proto.VariableNamingConvention.convert(): VariableNamingConvention {
    return when(this) {
        Proto.VariableNamingConvention.FLAT -> VariableNamingConvention.FLAT
        Proto.VariableNamingConvention.STRUCTURED -> VariableNamingConvention.STRUCTURED
        Proto.VariableNamingConvention.UNRECOGNIZED -> throw AssertionError()
    }
}

internal fun Int.asProtoUInt(): Proto.UInt {
    return Proto.UInt.newBuilder()
            .setValue(this)
            .build()
}

internal fun Int.asProtoInt(): Proto.UInt {
    return Proto.UInt.newBuilder()
            .setValue(this)
            .build()
}

internal fun Proto.IntRead.convert(): FmuIntegerRead {
    return FmuIntegerRead(value, status.convert())
}

internal fun Proto.IntListRead.convert(): FmuIntegerArrayRead {
    return FmuIntegerArrayRead(valuesList.toIntArray(), status.convert())
}

internal fun Proto.RealRead.convert(): FmuRealRead {
    return FmuRealRead(value, status.convert())
}

internal fun Proto.RealListRead.convert(): FmuRealArrayRead {
    return FmuRealArrayRead(valuesList.toDoubleArray(), status.convert())
}

internal fun Proto.StrRead.convert(): FmuStringRead {
    return FmuStringRead(value, status.convert())
}

internal fun Proto.StrListRead.convert(): FmuStringArrayRead {
    return FmuStringArrayRead(valuesList.toTypedArray(), status.convert())
}

internal fun Proto.BoolRead.convert(): FmuBooleanRead {
    return FmuBooleanRead(value, status.convert())
}

internal fun Proto.BoolListRead.convert(): FmuBooleanArrayRead {
    return FmuBooleanArrayRead(valuesList.toBooleanArray(), status.convert())
}

internal fun Proto.DefaultExperiment.convert(): DefaultExperiment {
    return DefaultExperiment(
            startTime = startTime,
            stopTime = stopTime,
            tolerance = tolerance,
            stepSize = stepSize
    )
}

internal fun Proto.Unknown.convert(): Unknown {
    return object: Unknown {
        override val dependencies: List<Int>
            get() = dependenciesList
        override val dependenciesKind: DependenciesKind?
            get() = getDependenciesKind()?.convert()
        override val index: Int
            get() = getIndex()
    }
}

internal fun Proto.ModelStructure.convert(): ModelStructure {
    return object: ModelStructure {
        override val derivatives: List<Unknown>
            get() = derivativesList?.map { it.convert() } ?: emptyList()
        override val initialUnknowns: List<Unknown>
            get() = initialUnknownsList?.map { it.convert() } ?: emptyList()
        override val outputs: List<Unknown>
            get() = outputsList?.map { it.convert() } ?: emptyList()
    }
}

internal fun Proto.IntegerAttribute.convert(): IntegerAttribute {
    return IntegerAttribute(
            min = min,
            max = max,
            start = start
    )
}

internal fun Proto.RealAttribute.convert(): RealAttribute {
    return RealAttribute(
            min = min,
            max = max,
            start = start
    )
}

internal fun Proto.StringAttribute.convert(): StringAttribute {
    return StringAttribute(
            start = start
    )
}

internal fun Proto.BooleanAttribute.convert(): BooleanAttribute {
    return BooleanAttribute(
            start = start
    )
}

internal fun Proto.EnumerationAttribute.convert(): EnumerationAttribute {
    return EnumerationAttribute(
            min = min,
            max = max,
            start = start
    )
}

internal fun Proto.ScalarVariable.convert(): TypedScalarVariable<*> {

    val v = ScalarVariableImpl(
            name = name,
            description = description,
            valueReference = valueReference,
            declaredType = declaredType,
            causality = causality?.convert(),
            variability = variability?.convert(),
            initial = initial?.convert()
    )

    when(attributeCase) {
        Proto.ScalarVariable.AttributeCase.INTEGERATTRIBUTE -> v.integerAttribute = integerAttribute.convert()
        Proto.ScalarVariable.AttributeCase.REALATTRIBUTE -> v.realAttribute = realAttribute.convert()
        Proto.ScalarVariable.AttributeCase.STRINGATTRIBUTE -> v.stringAttribute = stringAttribute.convert()
        Proto.ScalarVariable.AttributeCase.BOOLEANATTRIBUTE -> v.booleanAttribute = booleanAttribute.convert()
        Proto.ScalarVariable.AttributeCase.ENUMERATIONATTRIBUTE -> v.enumerationAttribute = enumerationAttribute.convert()
        else -> throw AssertionError()
    }

    return v.toTyped()

}

internal fun List<Proto.ScalarVariable>.convert(): ModelVariables {
    return object : ModelVariables {
        override val variables: List<TypedScalarVariable<*>> by lazy {
            this@convert.map { it.convert() }
        }
    }
}

internal fun Proto.ModelDescription.convert(): CommonModelDescription {
    return GrpcModelDescription(this)
}

class GrpcModelDescription(
        val modelDescription: Proto.ModelDescription
): CommonModelDescription {

    override val author: String?
        get() = modelDescription.author
    override val copyright: String?
        get() = modelDescription.copyright
    override val defaultExperiment: DefaultExperiment? =  modelDescription.defaultExperiment?.convert()
    override val description: String?
        get() = modelDescription.description
    override val fmiVersion: String
        get() = modelDescription.fmiVersion
    override val generationDateAndTime: String?
        get() = modelDescription.generationDateAndTime
    override val generationTool: String?
        get() = modelDescription.generationTool
    override val guid: String
        get() = modelDescription.guid
    override val license: String?
        get() = modelDescription.license
    override val logCategories: LogCategories?
        get() = null
    override val modelName: String
        get() = modelDescription.modelName
    override val modelStructure: ModelStructure = modelDescription.modelStructure.convert()
    override val modelVariables: ModelVariables = modelDescription.modelVariablesList.convert()
    override val supportsCoSimulation: Boolean
        get() = modelDescription.supportsCoSimulation
    override val supportsModelExchange: Boolean
        get() = modelDescription.supportsModelExchange
    override val variableNamingConvention: VariableNamingConvention? = modelDescription.variableNamingConvention?.convert()
    override val version: String?
        get() = modelDescription.version
}

fun Solver.protoType(): Proto.Solver {
    return Proto.Solver.newBuilder()
            .setName(name)
            .setSettings(settings)
            .build()
}