import requests
import details  # import details file with token and functions

# Get the list of files in your account

baseUrl = "https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com/sharebox/api/files?token="


# Validate the service returns 200 for a request with valid token ID
def test_get_files_service_returns_200():
    # Initiate a GET request and store the response in a variable
    response = requests.get(details.url(baseUrl), verify=False)  # verify=False to disable certificate validation
    assert response.status_code == 200  # assertion to validate the response status code to expected value


def test_get_files_service_returns_200_validate_response_body():
    response = requests.get(details.url(baseUrl), verify=False)
    response_body = response.json()  # Convert the response in to a Json Object
    assert response_body[0]["status"] == "Completed"
    ''' # other parameters can be validated as required
    # length = len(response_body)  # Get the length of the Json Object
    # We can get the values of the item in the Json Object, hence [length-value] to access it and validate
    assert response_body[0]["name"] == " File1"
    assert response_body[0]["fileHash"] == " hash"
    assert response_body[0]["createdOn"] == "2020-04-09 21:43:27"
    assert response_body[0]["bytesCompleted"] == 150
    assert response_body[0]["size"] == 150
    assert response_body[0]["fileId"] == "3617cfc6-9636-4258-9228-7d4be3cceac7"
    '''


# Validate whether service returns a  Unauthorized response for a request without token ID
def test_get_files_service_returns_401_no_token():
    response = requests.get(baseUrl, verify=False)  # Passed the Base URL without Token ID
    assert response.status_code == 401


def test_get_files_service_returns_401_invalid_token():
    response = requests.get(details.bad_url(baseUrl), verify=False)  # Passed the Base URL without an invalid Token ID
    assert response.status_code == 401
