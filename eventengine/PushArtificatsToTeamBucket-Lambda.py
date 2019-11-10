'''
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
'''

#This lambda function is used by Event Engine to push artifacts to S3 buckets inside Individual Teams 
import json
import urllib.parse
import boto3
from urllib.request import build_opener, HTTPHandler, Request

print('Loading function')

s3 = boto3.client('s3')

def lambda_handler(event, context):
    print('Received event: ' + json.dumps(event, indent=2))

    # Get the parameters from the event    
    source_bucket = event['ResourceProperties']['SourceBucket'] #This is the bucket for Event Engine
    source_prefix = event['ResourceProperties']['SourcePrefix'] #This is the key "modules/MODULE_ID/v1/"
    target_bucket = event['ResourceProperties']['WorkshopS3Bucket'] #Workshop Bucket in the Team account
    
    kwargs = {'Bucket': source_bucket}
    kwargs['Prefix'] = source_prefix
    try:
        resp = s3.list_objects_v2(**kwargs)
        contents = resp['Contents']
    except:
        print(e)
        error = 'Error getting objects from source bucket'
        send_response(event, context, 'FAILURE', {'Message': error})

    try:
        for obj in contents:
            key = obj['Key']
            copy_source = {'Bucket': source_bucket, 'Key': key}
            response = s3.copy_object(Bucket=target_bucket, Key=key.split('/')[-1], CopySource=copy_source)
            print(response)

        send_response(event, context, 'SUCCESS', {'Message': 'Success!'})
    except Exception as e:
        print(e)
        error = 'Error getting object {} from bucket {}. Make sure they exist and your bucket is in the same region as this function.'.format(key, source_bucket)
        print(error)
        send_response(event, context, 'FAILURE', {'Message': error})

def send_response(event, context, response_status, response_data):
    #Send a resource manipulation status response to CloudFormation
    response_body = json.dumps({
        'Status': response_status,
        'Reason': 'See the details in CloudWatch Log Stream: ' + context.log_stream_name,
        'PhysicalResourceId': context.log_stream_name,
        'StackId': event['StackId'],
        'RequestId': event['RequestId'],
        'LogicalResourceId': event['LogicalResourceId'],
        'Data': response_data
    })

    opener = build_opener(HTTPHandler)
    request = Request(event['ResponseURL'], data=response_body.encode('utf-8'))
    request.add_header('Content-Type', '')
    request.add_header('Content-Length', len(response_body))
    request.get_method = lambda: 'PUT'
    response = opener.open(request)
   