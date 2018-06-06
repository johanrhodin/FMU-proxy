// This autogenerated skeleton file illustrates how to build a server.
// You should copy it to another filename to avoid overwriting it.

#include "FmuService.h"
#include <thrift/protocol/TBinaryProtocol.h>
#include <thrift/server/TSimpleServer.h>
#include <thrift/transport/TServerSocket.h>
#include <thrift/transport/TBufferTransports.h>

using namespace ::apache::thrift;
using namespace ::apache::thrift::protocol;
using namespace ::apache::thrift::transport;
using namespace ::apache::thrift::server;

using namespace  ::fmuproxy::thrift;

class FmuServiceHandler : virtual public FmuServiceIf {
 public:
  FmuServiceHandler() {
    // Your initialization goes here
  }

  void getModelDescriptionXml(std::string& _return) {
    // Your implementation goes here
    printf("getModelDescriptionXml\n");
  }

  void getModelDescription( ::fmuproxy::thrift::ModelDescription& _return) {
    // Your implementation goes here
    printf("getModelDescription\n");
  }

  FmuId createInstanceFromCS() {
    // Your implementation goes here
    printf("createInstanceFromCS\n");
  }

  FmuId createInstanceFromME(const  ::fmuproxy::thrift::Solver& solver) {
    // Your implementation goes here
    printf("createInstanceFromME\n");
  }

  bool canGetAndSetFMUstate(const FmuId fmu_id) {
    // Your implementation goes here
    printf("canGetAndSetFMUstate\n");
  }

  double getCurrentTime(const FmuId fmu_id) {
    // Your implementation goes here
    printf("getCurrentTime\n");
  }

  bool isTerminated(const FmuId fmu_id) {
    // Your implementation goes here
    printf("isTerminated\n");
  }

   ::fmuproxy::thrift::Status::type init(const FmuId fmu_id, const double start, const double stop) {
    // Your implementation goes here
    printf("init\n");
  }

  void step( ::fmuproxy::thrift::StepResult& _return, const FmuId fmu_id, const double step_size) {
    // Your implementation goes here
    printf("step\n");
  }

   ::fmuproxy::thrift::Status::type terminate(const FmuId fmu_id) {
    // Your implementation goes here
    printf("terminate\n");
  }

   ::fmuproxy::thrift::Status::type reset(const FmuId fmu_id) {
    // Your implementation goes here
    printf("reset\n");
  }

  void readInteger( ::fmuproxy::thrift::IntegerRead& _return, const FmuId fmu_id, const ValueReference vr) {
    // Your implementation goes here
    printf("readInteger\n");
  }

  void bulkReadInteger( ::fmuproxy::thrift::IntegerArrayRead& _return, const FmuId fmu_id, const ValueReferences& vr) {
    // Your implementation goes here
    printf("bulkReadInteger\n");
  }

  void readReal( ::fmuproxy::thrift::RealRead& _return, const FmuId fmu_id, const ValueReference vr) {
    // Your implementation goes here
    printf("readReal\n");
  }

  void bulkReadReal( ::fmuproxy::thrift::RealArrayRead& _return, const FmuId fmu_id, const ValueReferences& vr) {
    // Your implementation goes here
    printf("bulkReadReal\n");
  }

  void readString( ::fmuproxy::thrift::StringRead& _return, const FmuId fmu_id, const ValueReference vr) {
    // Your implementation goes here
    printf("readString\n");
  }

  void bulkReadString( ::fmuproxy::thrift::StringArrayRead& _return, const FmuId fmu_id, const ValueReferences& vr) {
    // Your implementation goes here
    printf("bulkReadString\n");
  }

  void readBoolean( ::fmuproxy::thrift::BooleanRead& _return, const FmuId fmu_id, const ValueReference vr) {
    // Your implementation goes here
    printf("readBoolean\n");
  }

  void bulkReadBoolean( ::fmuproxy::thrift::BooleanArrayRead& _return, const FmuId fmu_id, const ValueReferences& vr) {
    // Your implementation goes here
    printf("bulkReadBoolean\n");
  }

   ::fmuproxy::thrift::Status::type writeInteger(const FmuId fmu_id, const ValueReference vr, const int32_t value) {
    // Your implementation goes here
    printf("writeInteger\n");
  }

   ::fmuproxy::thrift::Status::type bulkWriteInteger(const FmuId fmu_id, const ValueReferences& vr, const IntArray& value) {
    // Your implementation goes here
    printf("bulkWriteInteger\n");
  }

   ::fmuproxy::thrift::Status::type writeReal(const FmuId fmu_id, const ValueReference vr, const double value) {
    // Your implementation goes here
    printf("writeReal\n");
  }

   ::fmuproxy::thrift::Status::type bulkWriteReal(const FmuId fmu_id, const ValueReferences& vr, const RealArray& value) {
    // Your implementation goes here
    printf("bulkWriteReal\n");
  }

   ::fmuproxy::thrift::Status::type writeString(const FmuId fmu_id, const ValueReference vr, const std::string& value) {
    // Your implementation goes here
    printf("writeString\n");
  }

   ::fmuproxy::thrift::Status::type bulkWriteString(const FmuId fmu_id, const ValueReferences& vr, const StringArray& value) {
    // Your implementation goes here
    printf("bulkWriteString\n");
  }

   ::fmuproxy::thrift::Status::type writeBoolean(const FmuId fmu_id, const ValueReference vr, const bool value) {
    // Your implementation goes here
    printf("writeBoolean\n");
  }

   ::fmuproxy::thrift::Status::type bulkWriteBoolean(const FmuId fmu_id, const ValueReferences& vr, const BooleanArray& value) {
    // Your implementation goes here
    printf("bulkWriteBoolean\n");
  }

};

int main(int argc, char **argv) {
  int port = 9090;
  ::apache::thrift::stdcxx::shared_ptr<FmuServiceHandler> handler(new FmuServiceHandler());
  ::apache::thrift::stdcxx::shared_ptr<TProcessor> processor(new FmuServiceProcessor(handler));
  ::apache::thrift::stdcxx::shared_ptr<TServerTransport> serverTransport(new TServerSocket(port));
  ::apache::thrift::stdcxx::shared_ptr<TTransportFactory> transportFactory(new TBufferedTransportFactory());
  ::apache::thrift::stdcxx::shared_ptr<TProtocolFactory> protocolFactory(new TBinaryProtocolFactory());

  TSimpleServer server(processor, serverTransport, transportFactory, protocolFactory);
  server.serve();
  return 0;
}
