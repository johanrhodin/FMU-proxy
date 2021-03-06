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

import io.grpc.BindableService
import io.grpc.stub.StreamObserver

import no.mechatronics.sfi.fmi4j.common.FmiStatus

import no.mechatronics.sfi.fmuproxy.fmu.Fmus
import no.mechatronics.sfi.fmuproxy.grpc.services.GrpcFmuService

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class {{fmuName}}Service: {{fmuName}}ServiceGrpc.{{fmuName}}ServiceImplBase(), GrpcFmuService {

    {{dynamicMethods}}

    companion object {
        val LOG: Logger = LoggerFactory.getLogger({{fmuName}}Service::class.java.simpleName)
    }

}

internal fun FmiStatus.protoType(): Proto.Status {
    return when (this) {
        FmiStatus.OK -> Proto.Status.OK_STATUS
        FmiStatus.Warning -> Proto.Status.WARNING_STATUS
        FmiStatus.Discard -> Proto.Status.DISCARD_STATUS
        FmiStatus.Error -> Proto.Status.ERROR_STATUS
        FmiStatus.Fatal -> Proto.Status.FATAL_STATUS
        FmiStatus.Pending -> Proto.Status.PENDING_STATUS
        FmiStatus.NONE -> Proto.Status.UNRECOGNIZED
    }
}


