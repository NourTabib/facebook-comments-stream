# Generated by the gRPC Python protocol compiler plugin. DO NOT EDIT!
"""Client and server classes corresponding to protobuf-defined services."""
import grpc

from server import PredictionService_pb2 as server_dot_PredictionService__pb2


class PredictionServiceStub(object):
    """Missing associated documentation comment in .proto file."""

    def __init__(self, channel):
        """Constructor.

        Args:
            channel: A grpc.Channel.
        """
        self.PredictText = channel.unary_unary(
                '/PredictionService.PredictionService/PredictText',
                request_serializer=server_dot_PredictionService__pb2.Text.SerializeToString,
                response_deserializer=server_dot_PredictionService__pb2.Prediction.FromString,
                )


class PredictionServiceServicer(object):
    """Missing associated documentation comment in .proto file."""

    def PredictText(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')


def add_PredictionServiceServicer_to_server(servicer, server):
    rpc_method_handlers = {
            'PredictText': grpc.unary_unary_rpc_method_handler(
                    servicer.PredictText,
                    request_deserializer=server_dot_PredictionService__pb2.Text.FromString,
                    response_serializer=server_dot_PredictionService__pb2.Prediction.SerializeToString,
            ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
            'PredictionService.PredictionService', rpc_method_handlers)
    server.add_generic_rpc_handlers((generic_handler,))


 # This class is part of an EXPERIMENTAL API.
class PredictionService(object):
    """Missing associated documentation comment in .proto file."""

    @staticmethod
    def PredictText(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(request, target, '/PredictionService.PredictionService/PredictText',
            server_dot_PredictionService__pb2.Text.SerializeToString,
            server_dot_PredictionService__pb2.Prediction.FromString,
            options, channel_credentials,
            call_credentials, compression, wait_for_ready, timeout, metadata)
