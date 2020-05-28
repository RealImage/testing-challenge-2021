import binascii
import os
import requests
import time
import base64
import json
from selenium import webdriver
from requests.packages import urllib3
# Disable HTTPS Insecure warning
urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)


class ApiTests:

    def __init__(self):
        # self.driver = webdriver.Firefox(executable_path="geckodriver.exe")
        self.token = ""
        self.fileID = ""
        self.filename = ""
        self.headers = self.get_data_from_json("headers")
        self.boundary = binascii.hexlify(os.urandom(16)).decode('ascii')
        self.content_type = "multipart/form-data; boundary=" + self.boundary
        self.form_headers = {"Content-Type" : self.content_type, "Accept" : "application/json"}

    # Code to fetch data from json file
    def get_data_from_json(self, key):
        json_content = json.loads(open("input_data.json",).read())
        return json_content[key]

    # Code to generate token via gui
    def get_token_gui(self):
        json_content = self.get_data_from_json("token_generation")
        url = json_content["url"]
        username = json_content["uname"]
        password = base64.b64decode(json_content["passwd"]).decode("utf-8")
        self.driver.get(url)
        time.sleep(2)
        self.driver.find_element_by_id(json_content["username_txtfld_id"]).send_keys(username)
        self.driver.find_element_by_id(json_content["paswd_txtfld_id"]).send_keys(password)
        self.driver.find_element_by_xpath(json_content["login_btn_xpath"]).click()
        if self.driver.find_element_by_xpath(json_content["generate_token_xpath"]).is_displayed():
            time.sleep(2)
            self.driver.find_element_by_xpath(json_content["generate_token_xpath"]).click()
            if self.driver.find_element_by_id(json_content["generate_tkn_btn_id"]).is_displayed():
                time.sleep(2)
                self.driver.find_element_by_id(json_content["generate_tkn_btn_id"]).click()
                time.sleep(2)
                self.token = self.driver.find_element_by_xpath(json_content["new_token_id"]).text
                time.sleep(2)
                self.driver.close()
                return self.token

    # Code to generate token via API
    def get_token_api(self, token):
        api_uri = base_uri + "token?token=" + token
        resp = requests.get(api_uri, verify=False)
        response = json.loads(resp.json())
        return response[0]["token"]

    # Code to generate multipart form-data headers
    def generate_request_data(self, data):
        req_data = (
            "".join('--%s\r\n'
                    'Content-Disposition: form-data; name="%s"\r\n'
                        '\r\n'
                        '%s\r\n' % (self.boundary, field, value)
                        for field, value in data.items()) +
                "--%s--\r\n" % self.boundary
        )
        return req_data

    # Code to fetch user id based on user email
    def get_user_id_with_email(self, base_uri, token, email):
        api_uri = base_uri + "users?token=" + token
        resp = requests.get(api_uri, verify=False)
        for user in resp.json():
            if user["email"] == email:
                return user["id"]

    # Code to validate POST request for upload file
    def test_post_uploadfile(self, base_uri, token):
        test_data = self.get_data_from_json("post_uploadfile_data")
        test_uri = "token="+token
        api_uri = base_uri + "upload?" + test_uri
        test_data["hash"] = hash(self.boundary)
        self.filename = test_data["name"]
        test_data = json.dumps(test_data, ensure_ascii=False)
        test_data = self.generate_request_data(json.loads(test_data))
        print("Test : UploadFile : Post ")
        print("API URI : "+str(api_uri))

        resp = requests.post(api_uri, data=test_data, headers=self.form_headers, verify=False)
        response = resp.json()
        self.fileID = response['fileId']
        assert resp.status_code == 200, "Fail : Unable to validate 200 status code for Upload file - POST"
        print("Pass : Validated "+str(resp.status_code)+" status code for Upload file - POST")

        resp = requests.post(api_uri, headers=self.headers, verify=False)
        assert resp.status_code == 400, "Fail : Unable to validate 400 status code for Upload file - POST"
        print("Pass : Validated "+str(resp.status_code)+" status code for Upload file - POST")

        resp = requests.post(api_uri[:80], headers=self.headers, verify=False)
        assert resp.status_code == 401, "Fail : Unable to validate 401 status code for Upload file - POST"
        print("Pass : Validated "+str(resp.status_code)+" status code for Upload file - POST")

        resp = requests.post(base_uri, data=json.dumps(test_data), headers=self.headers, verify=False)
        assert resp.status_code == 404, "Fail : Unable to validate 404 status code for Upload file - POST"
        print("Pass : Validated " + str(resp.status_code) + " status code for Upload file - POST")
        print ("\n")

    # Code to validate PUT request for upload file to update the status
    def test_put_uploadfile(self, base_uri, token):
        test_data = self.get_data_from_json("put_uploadfile_data")
        test_data["fileId"] = self.fileID
        test_data = json.dumps(test_data, ensure_ascii=False)
        test_data = self.generate_request_data(json.loads(test_data))
        test_uri = "token="+token
        api_uri = base_uri + "upload?" + test_uri
        print("Test : UploadFile : Put")
        print("API URI : " + str(api_uri))

        resp = requests.put(api_uri, data=test_data, headers=self.form_headers, verify=False)
        err_msg = "Fail : Unable to validate message for Upload file - PUT"
        success_msg = "Bytes has been updated successfully"
        assert resp.status_code == 200, "Fail : Unable to validate 200 status code for Upload file - PUT"
        assert resp.json()["message"] == success_msg, err_msg
        print("Pass : Validated " + str(resp.status_code) + " status code and message for Upload file - PUT")

        resp = requests.put(api_uri, data=test_data, verify=False)
        assert resp.status_code == 400, "Fail : Unable to validate 400 status code for Upload file - PUT"
        print("Pass : Validated " + str(resp.status_code) + " status code for Upload file - PUT")

        resp = requests.put(api_uri[:80], data=test_data, verify=False)
        assert resp.status_code == 401, "Fail : Unable to validate 401 status code for Upload file - PUT"
        print("Pass : Validated " + str(resp.status_code) + " status code for Upload file - PUT")

        resp = requests.put(base_uri, data=test_data, headers=self.headers, verify=False)
        assert resp.status_code == 404, "Fail : Unable to validate 404 status code for Upload file - PUT"
        print("Pass : Validated " + str(resp.status_code) + " status code for Upload file - PUT")

        test_invalid_uri = base_uri + "upload?fileId=test1234&token="+token+"&bytesCompleted=30"
        resp = requests.put(test_invalid_uri, data=test_data, headers=self.headers, verify=False)
        assert resp.status_code == 500, "Fail : Unable to validate 500 status code for Upload file - PUT"
        print("Pass : Validated " + str(resp.status_code) + " status code for Upload file - PUT")
        print("\n")

    # Code to validate GET request for uploaded file
    def test_get_uploadfile(self, base_uri, token):
        test_data = self.get_data_from_json("get_uploadfile_data")
        test_data["fileId"] = self.fileID
        test_uri = "fileId="+test_data["fileId"]+"&token="+token
        api_uri = base_uri + "upload?" + test_uri
        print("Test : UploadFile : Get")
        print("API URI : "+str(api_uri))

        resp = requests.get(api_uri, verify=False)
        response = resp.json()
        assert response["status"] == "Completed", "Fail : Unable to validate status for Upload file - GET"
        assert resp.status_code == 200, "Fail : Unable to validate 200 status code for Upload file - GET"
        print("Pass : Validated " + str(resp.status_code) + " status message and status code for Upload file - GET")

        resp = requests.get(api_uri[:80], verify=False)
        assert resp.status_code == 401, "Fail : Unable to validate 401 status code for Upload file - GET"
        print("Pass : Validated " + str(resp.status_code) + " status code for Upload file - GET")

        resp = requests.get(base_uri, verify=False)
        assert resp.status_code == 404, "Fail : Unable to validate 404 status code for Upload file - GET"
        print("Pass : Validated " + str(resp.status_code) + " status code for Upload file - GET")

        test_invalid_uri = base_uri + "upload?fileId=test134&token="+token
        resp = requests.get(test_invalid_uri, verify=False)
        assert resp.status_code == 500, "Fail : Unable to validate 500 status code for Upload file - GET"
        print("Pass : Validated " + str(resp.status_code) + " status code for Upload file - GET")
        print("\n")

    # Code to validate PUT request to accept or reject shared file
    def test_put_sharefile(self, base_uri, token):
        test_data = self.get_data_from_json("put_sharefile_data")
        test_data["fileId"] = self.fileID
        test_uri = "token="+ token
        test_data = json.dumps(test_data, ensure_ascii=False)
        test_data = self.generate_request_data(json.loads(test_data))
        api_uri = base_uri + "files?" + test_uri
        print("Test : ShareFile : Put")
        print("API URI : " + str(api_uri))

        resp = requests.put(api_uri, data=test_data, headers=self.form_headers, verify=False)
        err_msg = "Fail : Unable to validate message for Share file - PUT"
        if self.get_data_from_json("put_sharefile_data")["isAccepted"] == "true":
            success_msg = "File Accepted"
            print("Pass : Validated File Accepted message for Share file - PUT")
        else:
            success_msg = "File Rejected"
            print("Pass : Validated File Rejected message for Share file - PUT")

        assert resp.json()["message"] == success_msg, err_msg
        assert resp.status_code == 200, "Fail : Unable to validate 200 status code for Share file - PUT"
        print("Pass : Validated " + str(resp.status_code) + " status code and message for Share file - PUT")

        resp = requests.put(api_uri, data=test_data, verify=False)
        assert resp.status_code == 400, "Fail : Unable to validate 400 status code for Share file - PUT"
        print("Pass : Validated " + str(resp.status_code) + " status code for Share file - PUT")

        resp = requests.put(api_uri[:80], data=test_data, verify=False)
        assert resp.status_code == 401, "Fail : Unable to validate 401 status code for Share file - PUT"
        print("Pass : Validated " + str(resp.status_code) + " status code for Share file - PUT")

        resp = requests.put(base_uri, data=test_data, headers=self.headers, verify=False)
        assert resp.status_code == 404, "Fail : Unable to validate 404 status code for Share file - PUT"
        print("Pass : Validated " + str(resp.status_code) + " status code for Share file - PUT")

        test_invalid_uri = base_uri + "upload?fileId=test1234&token="+token+"&bytesCompleted=30"
        resp = requests.put(test_invalid_uri, data=test_data, headers=self.headers, verify=False)
        assert resp.status_code == 500, "Fail : Unable to validate 500 status code for Share file - PUT"
        print("Pass : Validated " + str(resp.status_code) + " status code for Share file - PUT")
        print("\n")

    # Code to validate GET request for shared files
    def test_get_sharedfile(self, base_uri, token):
        test_uri = "token="+token
        api_uri = base_uri + "files?" + test_uri
        print("Test : SharedFiles : Get")
        print("API URI : "+str(api_uri))

        resp = requests.get(api_uri, verify=False)
        respone = resp.json()
        assert resp.status_code == 200, "Fail : Unable to validate 200 status code for Shared Files - GET"
        if self.filename:
            success_msg = "Pass : Validated file shared with another user"
            err_msg = "Fail : No file shared with another user"
            assert respone[0]["name"] == self.filename, err_msg
            print (success_msg)
        print("Pass : Validated " + str(resp.status_code) + " status code and file for Shared Files - GET")

        resp = requests.get(api_uri[:80], verify=False)
        assert resp.status_code == 401, "Fail : Unable to validate 401 status code for Shared Files - GET"
        print("Pass : Validated " + str(resp.status_code) + " status code for Shared Files - GET")

        resp = requests.get(base_uri, verify=False)
        assert resp.status_code == 404, "Fail : Unable to validate 404 status code for Shared Files - GET"
        print("Pass : Validated " + str(resp.status_code) + " status code for Shared Files - GET")
        print("\n")

    # Code to validate POST request for sharing file with another user
    def test_post_sharefile(self, base_uri, token):
        test_data = self.get_data_from_json("post_sharefile_data")
        test_data["fileId"] = self.fileID
        user_id = self.get_user_id_with_email(base_uri, token, test_data["shareTo"])
        test_data["shareTo"] = user_id
        test_uri = "token="+str(token)
        test_data = json.dumps(test_data, ensure_ascii=False)
        api_uri = base_uri + "files?" + test_uri
        print("Test : SharedFiles : Post")
        print("API URI : "+str(api_uri))
        payload = self.generate_request_data(json.loads(test_data))

        resp = requests.post(api_uri, data=payload, headers=self.form_headers, verify=False)
        success_msg = "Successfully Shared"
        err_msg = "Fail : Unable to validate message for Share file - POST"
        assert resp.json()["message"] == success_msg, err_msg
        assert resp.status_code == 200, "Fail : Unable to validate 200 status code for Share file - POST"
        print("Pass : Validated "+str(resp.status_code)+" status code and message for Share file - POST")

        resp = requests.post(api_uri, headers=self.form_headers, verify=False)
        assert resp.status_code == 400, "Fail : Unable to validate 400 status code for Share file - POST"
        print("Pass : Validated "+str(resp.status_code)+" status code for Share file - POST")

        resp = requests.post(api_uri[:80], headers=self.form_headers, verify=False)
        assert resp.status_code == 401, "Fail : Unable to validate 401 status code for Share file - POST"
        print("Pass : Validated "+str(resp.status_code)+" status code for Share file - POST")

        resp = requests.post(base_uri, data=test_data, headers=self.form_headers, verify=False)
        assert resp.status_code == 404, "Fail : Unable to validate 404 status code for Share file - POST"
        print("Pass : Validated " + str(resp.status_code) + " status code for Share file - POST")
        print ("\n")

    # Code to validate DELETE request for uploaded files
    def test_delete_file(self, base_uri, token):
        test_data = self.get_data_from_json("delete_file_data")
        test_data["fileId"] = self.fileID
        test_uri = "token="+token
        test_data = json.dumps(test_data, ensure_ascii=False)
        test_data = self.generate_request_data(json.loads(test_data))
        api_uri = base_uri + "files?" + test_uri
        print("Test : Files : Delete")
        print("API URI : "+str(api_uri))

        resp = requests.delete(api_uri, data=test_data, headers=self.form_headers, verify=False)
        assert resp.status_code == 200, "Fail : Unable to validate 200 status code for Files - DELETE"
        err_msg = "Fail : Unable to validate delete message for Files - DELETE"
        success_msg = "File deleted successfully"
        assert resp.json()["message"] == success_msg, err_msg
        print("Pass : Validated " + str(resp.status_code) + " status code and message for Files - DELETE")

        resp = requests.delete(api_uri[:80], verify=False)
        assert resp.status_code == 401, "Fail : Unable to validate 401 status code for Files - DELETE"
        print("Pass : Validated " + str(resp.status_code) + " status code for Files - DELETE")

        resp = requests.delete(base_uri, verify=False)
        assert resp.status_code == 404, "Fail : Unable to validate 404 status code for Files - DELETE"
        print("Pass : Validated " + str(resp.status_code) + " status code for Files - DELETE")
        print("\n")

tests = ApiTests()
base_uri = tests.get_data_from_json("base_uri")
# token = tests.get_token_api(tests.get_data_from_json("token_user1"))
# token1 = tests.get_token_api(tests.get_data_from_json("token_user2"))
token = tests.get_data_from_json("token_user1")
token1 = tests.get_data_from_json("token_user2")
tests.test_post_uploadfile(base_uri, token)
tests.test_put_uploadfile(base_uri, token)
tests.test_get_uploadfile(base_uri, token)
tests.test_post_sharefile(base_uri, token)
# Validating if files shared is getting listed for user-1
tests.test_get_sharedfile(base_uri, token)
# Accepting/Rejecting file shared by user-1
tests.test_put_sharefile(base_uri, token1)
# Validating if file share accepted is getting listed for user-2
tests.test_get_sharedfile(base_uri, token1)
# Deleting files for user-1 and user-2
tests.test_delete_file(base_uri, token)
tests.test_delete_file(base_uri, token1)
