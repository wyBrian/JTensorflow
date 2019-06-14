package brian.wang.tf.serving;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.io.File;
import org.apache.commons.io.FileUtils;
import java.io.IOException;

import org.tensorflow.framework.DataType;
import org.tensorflow.framework.TensorProto;
import org.tensorflow.framework.TensorShapeProto;

import tensorflow.serving.Model;
import tensorflow.serving.Predict;
import tensorflow.serving.PredictionServiceGrpc;



public class DemoApplication {

    public static void main(String[] args){
        byte[] fileContent = new byte[0];
        try {

            fileContent = FileUtils.readFileToByteArray(new File("./src/main/resources/images/car.jpg"));
            String host = "127.0.0.1";
            int port = 8500;
            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).maxInboundMessageSize(1073741824).build();
            PredictionServiceGrpc.PredictionServiceBlockingStub stub = PredictionServiceGrpc.newBlockingStub(channel);

            Predict.PredictRequest.Builder predictRequestBuilder = Predict.PredictRequest.newBuilder();
            Model.ModelSpec.Builder modelSpec = Model.ModelSpec.newBuilder();
            modelSpec.setName("resnet");
            predictRequestBuilder.setModelSpec(modelSpec);


            TensorProto.Builder tensorProto = TensorProto.newBuilder();
            TensorShapeProto.Builder tensorShapeBuilder = TensorShapeProto.newBuilder().addDim(TensorShapeProto.Dim.newBuilder().setSize(1));

            tensorProto.setDtype(DataType.DT_STRING);
            tensorProto.setTensorShape(tensorShapeBuilder.build());
            tensorProto.addStringVal(ByteString.copyFrom(fileContent));

            predictRequestBuilder.putInputs("image_bytes", tensorProto.build());
            Predict.PredictResponse predictResponse = stub.predict(predictRequestBuilder.build());

            System.out.println("classes are: " + predictResponse.getOutputsOrThrow("classes").getInt64Val(0));
            System.out.println("probabilities are: " + predictResponse.getOutputsOrThrow("probabilities").getFloatVal(0));

            channel.shutdown();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
