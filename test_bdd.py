from pytest_bdd import scenario, given, when, then
import pytest
from help import helper
from config import confi

@scenario('./Tests_bdd/features/sharebox.feature',"File Upload and get")
def test_add():
    pass

@pytest.fixture
@given("user has a file with <token> and <file> and <size> and <hash> and <name>")
def prep_data(token,file,size,hash,name):
    data={"params":{"token":token},
        "body":{
            "file":file,
            "size":size,
            "hash":hash,
            "name":name}}
    return data

@when("user uploads file")
def upload_file(prep_data):
    url=confi.BASE_URI+confi.endpoints["upload"]
    response=helper.post(url,prep_data["params"],prep_data["body"])
    assert response.status_code is 200
    json_res=response.json()
    prep_data["fileId"]=json_res["fileId"]

    
@then("user retrieves file")
def get_file():
    url=confi.BASE_URI+confi.endpoints["upload"]
    params={
        "token":prep_data["params"]["token"],
        "fileId":prep_data["fileId"]

    }
    response=helper.get(url,params)
    assert response.status_code is 200
