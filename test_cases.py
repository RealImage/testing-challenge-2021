import requests
import json
from jsonschema import validate
from pprint import pprint


# Get the list of files in your account
# url to be changed
BASE_URL = "https://ec2-15-206-185-35.ap-south-1.compute.amazonaws.com/sharebox/api/"
GET_FILES_ENDPOINT = "files?token="
UPLOAD_FILES_ENDPOINT = "upload?token="
MULTIPART_FORM_DATA = 'multipart/form-data; boundary=BOUNDARY'
APPLICATION_JSON = 'application/json'

test_file = open('E:\\RestAPIs\\test_Cases\\test_file.txt', 'r')

schema = {
    "allOf": [
        {
            "$ref": "#/definitions/GetFiles"
        }
    ],
    "definitions": {
        "GetFiles": {
            "type": "array",
            "items": {
                "$ref": "#/definitions/GetUploadDetails"
            }
        },
        "GetUploadDetails": {
            "type": "object",
            "properties": {
                "status": {
                    "type": "string"
                },
                "name": {
                    "type": "string"
                },
                "fileHash": {
                    "type": "string"
                },
                "createdOn": {
                    "type": "string"
                },
                "bytesCompleted": {
                    "type": "string"
                },
                "size": {
                    "type": "string"
                },
                "fileId": {
                    "type": "string"
                }
            }
        }
    }
}

error_schema = {
    "type": "object",
    "properties": {
        "error": {
            "type": "string"
        }
    }
}

upload_schema = {
    "type": "object",
    "properties": {
        "fileId": {
            "type": "string"
        },
        "accessKey": {
            "type": "string"
        },
        "secretKey": {
            "type": "string"
        },
        "sessionToken": {
            "type": "string"
        }
    }
}

delete_schema = {
  "type": "object",
  "required": [
    "message"
  ],
  "properties": {
    "message": {
      "type": "string",
      "description": "File deleted successfully"
    }
  },
  "$schema": "http://json-schema.org/draft-04/schema#"
}

TOKEN = "c4911cec-592a-4703-927d-f013907331cb"
UNAUTHORIZED_TOKEN = "rf91-592a-4703-927d-f013907331cb"
FORBIDDEN_TOKEN = "c4911cec-592a-4703-927d-f013907331cb"

# GET
def test_get_files_with_shared_files():
    get_url = BASE_URL + "files?getSharedFiles=test&token=" + TOKEN
    get_inbox_file_response = requests.get(get_url)
    get_json_body = get_inbox_file_response.json()

    assert get_inbox_file_response.status_code == 200
    validate(instance=get_json_body, schema=schema)
    pprint(get_inbox_file_response.request)
    pprint(get_inbox_file_response)

def test_get_files_without_shared_files():
    get_url = BASE_URL + GET_FILES_ENDPOINT + TOKEN
    get_inbox_response = requests.get(get_url)
    get_json_body = get_inbox_response.json()

    assert get_inbox_response.status_code == 200
    validate(instance=get_json_body, schema=schema)
    pprint(get_inbox_response.request)
    pprint(get_inbox_response)


def test_get_files_unauthorized():
    get_url = BASE_URL + GET_FILES_ENDPOINT + UNAUTHORIZED_TOKEN
    get_inbox_response = requests.get(get_url)
    get_json_body = get_inbox_response.json()

    assert get_inbox_response.status_code == 401
    validate(instamce=get_json_body, schema=error_schema)
    pprint(get_inbox_response.request)
    pprint(get_inbox_response)


def test_get_files_forbidden():
    get_url = BASE_URL + GET_FILES_ENDPOINT + FORBIDDEN_TOKEN
    get_inbox_response = requests.get(get_url)
    get_json_body = get_inbox_response.json()

    assert get_inbox_response.status_code == 403
    validate(instamce=get_json_body, schema=error_schema)
    pprint(get_inbox_response.request)
    pprint(get_inbox_response)



def test_get_files_tnternal_error():
    get_url = BASE_URL + GET_FILES_ENDPOINT + TOKEN
    get_inbox_response = requests.get(get_url)
    get_json_body = get_inbox_response.json()

    assert get_inbox_response.status_code == 500
    validate(instance=get_json_body, schema=error_schema)
    pprint(get_inbox_response.request)
    pprint(get_inbox_response)

# UPLOAD
def test_upload_file():
    upload_url = BASE_URL + UPLOAD_FILES_ENDPOINT + TOKEN

    headers = {
        'Content-Type': MULTIPART_FORM_DATA,
        'Accept': APPLICATION_JSON
    }

    file = {
        "name": "NAME1",
        "size": "13MB",
        "hash": "#xyzweb",
        "file": test_file
    }

    uploaded_file_response = requests.post(
        upload_url, files=file, headers=headers)

    post_json_body = uploaded_file_response.json()

    assert uploaded_file_response.status_code == 200
    assert uploaded_file_response.headers["Content-Type"] == APPLICATION_JSON

    validate(instance=post_json_body, schema=upload_schema)
    pprint(uploaded_file_response.request)
    pprint(uploaded_file_response)


def test_file_upload_incorrect_payload():
    upload_url = BASE_URL + UPLOAD_FILES_ENDPOINT + TOKEN

    file = {
        "name": "NAME1",
        "length": "13MB",
        "hash": "#xyzweb",
        "file_name": test_file
    }

    headers = {
        'Content-Type': MULTIPART_FORM_DATA,
        'Accept': APPLICATION_JSON
    }

    uploaded_file_response = requests.post(
        upload_url, files=file, headers=headers)

    post_json_body = uploaded_file_response.json()

    assert uploaded_file_response.status_code == 400
    assert uploaded_file_response.headers["Content-Type"] == APPLICATION_JSON

    validate(instance=post_json_body, schema=error_schema)
    pprint(uploaded_file_response.request)
    pprint(uploaded_file_response)

def test_file_upload_missing_name_payload_attribute():
    upload_url = BASE_URL + UPLOAD_FILES_ENDPOINT + TOKEN

    file = {
        "size": "13MB",
        "hash": "#xyzweb",
        "file": test_file
    }

    headers = {
        'Content-Type': MULTIPART_FORM_DATA,
        'Accept': APPLICATION_JSON
    }

    uploaded_file_response = requests.post(
        upload_url, files=file, headers=headers)

    post_json_body = uploaded_file_response.json()

    assert uploaded_file_response.status_code == 400
    assert uploaded_file_response.headers["Content-Type"] == APPLICATION_JSON

    validate(instance=post_json_body, schema=error_schema)
    pprint(uploaded_file_response.request)
    pprint(uploaded_file_response)

def test_file_upload_missing_size_payload_attribute():
    upload_url = BASE_URL + UPLOAD_FILES_ENDPOINT + TOKEN

    file = {
        "name": "NAME1",
        "hash": "#xyzweb",
        "file": test_file
    }

    headers = {
        'Content-Type': MULTIPART_FORM_DATA,
        'Accept': APPLICATION_JSON
    }

    uploaded_file_response = requests.post(
        upload_url, files=file, headers=headers)

    post_json_body = uploaded_file_response.json()

    assert uploaded_file_response.status_code == 400
    assert uploaded_file_response.headers["Content-Type"] == APPLICATION_JSON

    validate(instance=post_json_body, schema=error_schema)
    pprint(uploaded_file_response.request)
    pprint(uploaded_file_response)

def test_file_upload_missing_hash_payload_attribute():
    upload_url = BASE_URL + UPLOAD_FILES_ENDPOINT + TOKEN

    file = {
        "name": "NAME1",
        "size": "13MB",
        "file": test_file
    }

    headers = {
        'Content-Type': MULTIPART_FORM_DATA,
        'Accept': APPLICATION_JSON
    }

    uploaded_file_response = requests.post(
        upload_url, files=file, headers=headers)

    post_json_body = uploaded_file_response.json()

    assert uploaded_file_response.status_code == 400
    assert uploaded_file_response.headers["Content-Type"] == APPLICATION_JSON

    validate(instance=post_json_body, schema=error_schema)
    pprint(uploaded_file_response.request)
    pprint(uploaded_file_response)

def test_file_upload_missing_file_payload_attribute():
    upload_url = BASE_URL + UPLOAD_FILES_ENDPOINT + TOKEN

    file = {
        "name": "NAME1",
        "size": "13MB",
        "hash": "#xyzweb",
    }

    headers = {
        'Content-Type': MULTIPART_FORM_DATA,
        'Accept': APPLICATION_JSON
    }

    uploaded_file_response = requests.post(
        upload_url, files=file, headers=headers)

    post_json_body = uploaded_file_response.json()

    assert uploaded_file_response.status_code == 400
    assert uploaded_file_response.headers["Content-Type"] == APPLICATION_JSON

    validate(instance=post_json_body, schema=error_schema)
    pprint(uploaded_file_response.request)
    pprint(uploaded_file_response)

def test_file_upload_unauthorized():
    upload_url = BASE_URL + UPLOAD_FILES_ENDPOINT + UNAUTHORIZED_TOKEN

    file = {
        "name": "NAME1",
        "size": "13MB",
        "hash": "#xyzweb",
        "file": test_file
    }

    headers = {
        'Content-Type': MULTIPART_FORM_DATA,
        'Accept': APPLICATION_JSON
    }

    uploaded_file_response = requests.post(
        upload_url, files=file, headers=headers)

    post_json_body = uploaded_file_response.json()

    assert uploaded_file_response.status_code == 401
    assert uploaded_file_response.headers["Content-Type"] == APPLICATION_JSON

    validate(instance=post_json_body, schema=error_schema)
    pprint(uploaded_file_response.request)
    pprint(uploaded_file_response)

def test_file_upload_forbidden():
    upload_url = BASE_URL + UPLOAD_FILES_ENDPOINT + FORBIDDEN_TOKEN

    file = {
        "name": "NAME1",
        "size": "13MB",
        "hash": "#xyzweb",
        "file": test_file
    }

    headers = {
        'Content-Type': MULTIPART_FORM_DATA,
        'Accept': APPLICATION_JSON
    }

    uploaded_file_response = requests.post(
        upload_url, files=file, headers=headers)

    post_json_body = uploaded_file_response.json()

    assert uploaded_file_response.status_code == 403
    assert uploaded_file_response.headers["Content-Type"] == APPLICATION_JSON

    validate(instance=post_json_body, schema=error_schema)
    pprint(uploaded_file_response.request)
    pprint(uploaded_file_response)

def test_file_upload_internal_error():
    upload_url = BASE_URL + UPLOAD_FILES_ENDPOINT + TOKEN

    file = {
        "name": "NAME1",
        "size": "13MB",
        "hash": "#xyzweb",
        "file": test_file
    }

    headers = {
        'Content-Type': MULTIPART_FORM_DATA,
        'Accept': APPLICATION_JSON
    }

    uploaded_file_response = requests.post(
        upload_url, files=file, headers=headers)

    post_json_body = uploaded_file_response.json()

    assert uploaded_file_response.status_code == 500
    assert uploaded_file_response.headers["Content-Type"] == APPLICATION_JSON

    validate(instance=post_json_body, schema=error_schema)
    pprint(uploaded_file_response.request)
    pprint(uploaded_file_response)

# DELETE
def test_delete_file():
    delete_url = BASE_URL + GET_FILES_ENDPOINT + TOKEN

    payload = {
        "fileId": test_file

    }

    headers = {
        'Content-Type': MULTIPART_FORM_DATA,
        'Accept': APPLICATION_JSON
    }

    delete_file_response = requests.delete(
        delete_url, files=payload, headers=headers)

    delete_json_body = delete_file_response.json()

    assert delete_file_response.status_code == 200
    assert delete_file_response.headers["Content-Type"] == APPLICATION_JSON

    validate(instance=delete_json_body, schema=delete_schema)
    pprint(delete_file_response.request)
    pprint(delete_file_response)

def test_delete_file_unauthorized():
    delete_url = BASE_URL + GET_FILES_ENDPOINT + UNAUTHORIZED_TOKEN

    payload = {
        "fileId": test_file
    }

    headers = {
        'Content-Type': MULTIPART_FORM_DATA,
        'Accept': APPLICATION_JSON
    }

    delete_file_response = requests.delete(
        delete_url, files=payload, headers=headers)

    assert delete_file_response.status_code == 401
    assert delete_file_response.headers["Content-Type"] == APPLICATION_JSON
    pprint(delete_file_response.request)
    pprint(delete_file_response)

    
def test_file_upload_count():
    #Get number of files before upload
    get_url = BASE_URL + GET_FILES_ENDPOINT + TOKEN
    get_inbox_response = requests.get(get_url)
    number_of_files_before_upload = len(get_inbox_response.json())
    
    #Upload a file and capture the fileId
    upload_url = BASE_URL + UPLOAD_FILES_ENDPOINT + TOKEN

    file = {
        "name": "NAME1",
        "size": "13MB",
        "hash": "#xyzweb",
        "file": test_file
    }

    headers = {
        'Content-Type': MULTIPART_FORM_DATA,
        'Accept': APPLICATION_JSON
    }

    uploaded_file_response = requests.post(
        upload_url, files=file, headers=headers)

    post_json_body = uploaded_file_response.json()
    file_id = post_json_body["fileId"]

    assert uploaded_file_response.status_code == 200
    assert uploaded_file_response.headers["Content-Type"] == APPLICATION_JSON
    validate(instance=post_json_body, schema=error_schema)

    #Get the number of files after file upload
    get_url = BASE_URL + GET_FILES_ENDPOINT + TOKEN
    get_inbox_response = requests.get(get_url)
    number_of_files_after_upload = len(get_inbox_response.json())

    assert number_of_files_after_upload == number_of_files_before_upload+1

    #Delete the uploaded file
    delete_url = BASE_URL + GET_FILES_ENDPOINT + TOKEN

    payload = {
        "fileId": file_id

    }

    headers = {
        'Content-Type': MULTIPART_FORM_DATA,
        'Accept': APPLICATION_JSON
    }

    delete_file_response = requests.delete(
        delete_url, files=payload, headers=headers)

    assert delete_file_response.status_code == 200
    assert delete_file_response.headers["Content-Type"] == APPLICATION_JSON

    #Get file count after file deletion
    get_url = BASE_URL + GET_FILES_ENDPOINT + TOKEN
    get_inbox_response = requests.get(get_url)
    number_of_files_after_delete = len(get_inbox_response.json())
    
    assert number_of_files_after_delete == number_of_files_before_upload
