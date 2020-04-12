import requests
import details  # import details file with token and functions

# Upload any file into your account

baseUrl = "https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com/sharebox/api/upload?token="

payload = details.encode_multipart_formdata({"name": "FirstOne ", "size": "100", "hash": hash(details.boundary), "file": "sample1.txt"})

headers = {"content-Type": details.encode_multipart_headers()}


def test_upload_files_post_service_returns_200():
    # verify=False to disable certificate validation
    response = requests.post(details.url(baseUrl), data=payload, headers=headers, verify=False)
    assert response.status_code == 200


def test_upload_files_post_service_returns_400_no_payload():
    # verify=False to disable certificate validation
    response = requests.post(details.url(baseUrl), headers=headers, verify=False)
    assert response.status_code == 400


def test_upload_files_post_service_returns_400_bad_payload():
    # Prep a bad payload - without required parameters
    bad_payload = details.encode_multipart_formdata({"name": "FirstOne ", "size": "100"})
    # verify=False to disable certificate validation
    response = requests.post(details.url(baseUrl), data=bad_payload, headers=headers, verify=False)
    assert response.status_code == 400


def test_upload_files_post_service_returns_401_no_token():
    # verify=False to disable certificate validation
    response = requests.post(baseUrl, data=payload, headers=headers, verify=False)
    assert response.status_code == 401


def test_upload_files_post_service_returns_401_bad_token():
    # verify=False to disable certificate validation
    response = requests.post(details.bad_url(baseUrl), data=payload, headers=headers, verify=False)
    assert response.status_code == 401


def test_upload_files_post_service_returns_200_payload_as_json():
    # prep a payload in dict format
    dict_payload = {"name": "FirstOne ", "size": "100", "hash": hash(details.boundary), "file": "sample1.txt"}
    response = requests.post(details.url(baseUrl), data=dict_payload, verify=False)
    assert response.status_code == 200
