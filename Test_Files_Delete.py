import requests
import details  # import details file with token and functions

# Delete any file from your account

baseUrl = 'https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com/sharebox/api/files?token='

payload = details.encode_multipart_formdata({"fileId": "f993e16e-a4dc-4c85-99b2-8ae833ead042"})

headers = {"content-Type": details.encode_multipart_headers()}


def test_files_delete_service_returns_200():
    # verify=False to disable certificate validation
    response = requests.delete(details.url(baseUrl), data=payload, headers=headers, verify=False)
    assert response.status_code == 200


def test_files_delete_service_returns_400_no_headers():
    # verify=False to disable certificate validation
    response = requests.delete(details.url(baseUrl), data=payload, verify=False)
    assert response.status_code == 400


def test_files_delete_service_returns_400_without_payload():
    # verify=False to disable certificate validation
    response = requests.delete(details.url(baseUrl), headers=headers, verify=False)
    assert response.status_code == 400


def test_files_delete_service_returns_401_no_token():
    # verify=False to disable certificate validation
    response = requests.delete(baseUrl, data=payload, headers=headers, verify=False)
    assert response.status_code == 401


def test_files_delete_service_returns_401_invalid_token():
    # verify=False to disable certificate validation
    response = requests.delete(details.bad_url(baseUrl), data=payload, headers=headers, verify=False)
    assert response.status_code == 401
