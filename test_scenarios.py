import pytest
from jsonschema import validate, ValidationError, SchemaError
from help import helper
from config import confi
from config import testdata as td
import json

def test_uploadfile():
    url=confi.BASE_URI+confi.endpoints["upload"]
    response=helper.post(url,td.upload_file["params"],td.upload_file["body"])
    assert response.status_code is 200
    is_valid=helper.validateschema(response.json(),confi.upload_schema)
    assert is_valid is True

def test_getfile():
    url=confi.BASE_URI+confi.endpoints["upload"]
    response=helper.get(url,td.get_file["params"])
    assert response.status_code is 200
    is_valid=helper.validateschema(respose.json(),confi.get_schema)
    assert is_valid is True

def test_getallfiles():
    url=confi.BASE_URI+confi.endpoints["files"]
    response=helper.get(url,td.get_allfiles["params"])
    assert response.status_code is 200
    is_valid=helper.validateschema(response.json(),confi.getall_schema)
    assert is_valid is True

def test_getallfilesshared():
    url=confi.BASE_URI+confi.endpoints["files"]
    response=helper.get(url,td.get_sharedfiles["params"])
    assert response.status_code is 200
    is_valid=helper.validateschema(response.json(),confi.getall_schema)
    assert is_valid is True

