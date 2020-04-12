import requests
import details

# Check the uploaded files getting listed in your account

# values to be used in the GET service for a valid and invalid file request
fileId = 'cdc99b28-7879-4dc9-86aa-961ffef55271'
invalid_fileId = 'cdc99b28-7879-4dc9-86aa-961ffef55272'

# function to prep the URL based on input file value
def baseUrl(text):
    url1 = "https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com/sharebox/api/upload?fileId="
    url2 = url1 + text + "&token="
    return url2

# variables for valid and invalid URLs to be used in the requests
valid_url = baseUrl(fileId)
invalid_url = baseUrl(invalid_fileId)

def test_upload_files_get_service_returns_200():
    response = requests.get(details.url(valid_url), verify=False)  # verify=False to disable certificate validation
    assert response.status_code == 200  # assertion to validate the response status code to expected value


def test_upload_files_get_service_returns_401_no_token():
    response = requests.get(valid_url, verify=False)  # verify=False to disable certificate validation
    assert response.status_code == 401  # assertion to validate the response status code to expected value

def test_upload_files_get_service_returns_401_invalid_token():
    response = requests.get(details.bad_url(valid_url), verify=False)  # verify=False to disable certificate validation
    assert response.status_code == 401  # assertion to validate the response status code to expected value

def test_upload_files_get_service_returns_500():
    response = requests.get(details.url(invalid_url), verify=False)  # verify=False to disable certificate validation
    assert response.status_code == 500  # assertion to validate the response status code to expected value
