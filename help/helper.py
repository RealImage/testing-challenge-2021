import requests
from jsonschema import validate, ValidationError, SchemaError
# Making a get request
  
# prinitng request text
print()

def get(url,params):
    response=requests.get(url,params=params,verify=False)
    return response

def post(url,params,body):
    response=requests.post(url,params=params,data=body,verify=False)
    return response

def validateschema(actual,expected):
    valid=True
    try:
        validate(actual, expected)
    except:
        valid=False
    return valid
    