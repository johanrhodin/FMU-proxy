// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: service.proto

package no.mechatronics.sfi.grpc_fmu;

public final class GenericFmuProto {
  private GenericFmuProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\rservice.proto\022\034no.mechatronics.sfi.grp" +
      "c_fmu\032\021definitions.proto2\372\010\n\021GenericFmuS" +
      "ervice\022c\n\016CreateInstance\022#.no.mechatroni" +
      "cs.sfi.grpc_fmu.Empty\032,.no.mechatronics." +
      "sfi.grpc_fmu.ModelReference\022V\n\014GetModelN" +
      "ame\022#.no.mechatronics.sfi.grpc_fmu.Empty" +
      "\032!.no.mechatronics.sfi.grpc_fmu.Str\022c\n\025G" +
      "etModelVariableNames\022#.no.mechatronics.s" +
      "fi.grpc_fmu.Empty\032%.no.mechatronics.sfi." +
      "grpc_fmu.StrList\022g\n\021GetModelVariables\022#." +
      "no.mechatronics.sfi.grpc_fmu.Empty\032-.no." +
      "mechatronics.sfi.grpc_fmu.ScalarVariable" +
      "s\022b\n\016GetCurrentTime\022,.no.mechatronics.sf" +
      "i.grpc_fmu.ModelReference\032\".no.mechatron" +
      "ics.sfi.grpc_fmu.Real\022`\n\014IsTerminated\022,." +
      "no.mechatronics.sfi.grpc_fmu.ModelRefere" +
      "nce\032\".no.mechatronics.sfi.grpc_fmu.Bool\022" +
      "U\n\004Init\022).no.mechatronics.sfi.grpc_fmu.I" +
      "nitRequest\032\".no.mechatronics.sfi.grpc_fm" +
      "u.Bool\022W\n\004Step\022).no.mechatronics.sfi.grp" +
      "c_fmu.StepRequest\032$.no.mechatronics.sfi." +
      "grpc_fmu.Status\022`\n\tTerminate\022..no.mechat" +
      "ronics.sfi.grpc_fmu.TerminateRequest\032#.n" +
      "o.mechatronics.sfi.grpc_fmu.Empty\022Y\n\005Res" +
      "et\022*.no.mechatronics.sfi.grpc_fmu.ResetR" +
      "equest\032$.no.mechatronics.sfi.grpc_fmu.St" +
      "atus\022P\n\004Read\022%.no.mechatronics.sfi.grpc_" +
      "fmu.VarRead\032!.no.mechatronics.sfi.grpc_f" +
      "mu.Var\022U\n\005Write\022&.no.mechatronics.sfi.gr" +
      "pc_fmu.VarWrite\032$.no.mechatronics.sfi.gr" +
      "pc_fmu.StatusB\021B\017GenericFmuProtob\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          no.mechatronics.sfi.grpc_fmu.FmiDefinitions.getDescriptor(),
        }, assigner);
    no.mechatronics.sfi.grpc_fmu.FmiDefinitions.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
