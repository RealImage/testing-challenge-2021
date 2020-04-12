import requests
import details  # import details file with token and functions

# Approve/Reject the shared file from the receiver account

''' # this code can be used to dynamically pick and choose file for sharing
# function to get the list of files for a user, select a file and share with another user
def get_file_and_share_file():
    getfiles_baseUrl = "https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com/sharebox/api/files?token="
    response1 = requests.get(details.url(getfiles_baseUrl), verify=False)  # verify=False to disable certificate validation
    response_body = response1.json()
    fileId = response_body[0]["fileId"] # Choose the first file from user 1 to be shared
    sharefile_baseUrl = 'https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com/sharebox/api/files?token='
    payload2 = details.encode_multipart_formdata({"fileId": fileId, "shareTo": "selva.exe@gmail.com"})
    headers2 = {"content-Type": details.encode_multipart_headers()}
    # use the post request method to share the file from user 1 to user 2
    response2 = requests.post(details.url(sharefile_baseUrl), data=payload2, headers=headers2, verify=False)
    return fileId
'''

baseUrl = 'https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com/sharebox/api/files?token='

fileId = "cdc99b28-7879-4dc9-86aa-961ffef55271"


# to be used in conjunction with the function commented above
# payload1 = details.encode_multipart_formdata({"fileId": get_file_and_share_file(), "isAccepted": "true"})

# prep the payload for accept share scenario
payload1 = details.encode_multipart_formdata({"fileId": fileId, "isAccepted": "true"})
headers = {"content-Type": details.encode_multipart_headers()}


def test_files_put_service_returns_200_accept_share():
    # verify=False to disable certificate validation
    # calling the url_second() since the accept/decline would happen from user 2
    response = requests.put(details.url_second(baseUrl), data=payload1, headers=headers, verify=False)
    assert response.status_code == 200


# to be used in conjunction with the function commented above
# payload2 = details.encode_multipart_formdata({"fileId": get_file_and_share_file(), "isAccepted": "false"})

# prep the payload for decline scenario
payload2 = details.encode_multipart_formdata({"fileId": fileId, "isAccepted": "false"})
# noinspection PyRedeclaration
headers = {"content-Type": details.encode_multipart_headers()}


def test_files_put_service_returns_200_decline_share():
    # verify=False to disable certificate validation
    response = requests.put(details.url_second(baseUrl), data=payload2, headers=headers, verify=False)
    assert response.status_code == 200


def test_files_put_service_returns_400_no_headers():
    # verify=False to disable certificate validation
    response = requests.put(details.url_second(baseUrl), data=payload1, verify=False)
    assert response.status_code == 400


def test_files_put_service_returns_400_without_payload():
    # verify=False to disable certificate validation
    response = requests.put(details.url(baseUrl), headers=headers, verify=False)
    assert response.status_code == 400


def test_files_put_service_returns_401_no_token():
    # verify=False to disable certificate validation
    response = requests.put(baseUrl, data=payload1, headers=headers, verify=False)
    assert response.status_code == 401


def test_files_put_service_returns_401_invalid_token():
    # verify=False to disable certificate validation
    response = requests.put(details.bad_url(baseUrl), data=payload1, headers=headers, verify=False)
    assert response.status_code == 401
