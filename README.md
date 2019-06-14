# Java Tensorflow Serving Grpc Client Demo



## Download Demo Resnet Model


`mkdir models`

`mkdir models/resnet`

`curl -s https://storage.googleapis.com/download.tensorflow.org/models/official/20181001_resnet/savedmodels/resnet_v2_fp32_savedmodel_NHWC_jpg.tar.gz | tar --strip-components=2 -C ./models/resnet -xvz`

## To Test Grpc Client

`brian.wang.tf.serving.DemoApplication`