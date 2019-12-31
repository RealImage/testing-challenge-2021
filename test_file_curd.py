import definitions_requests as req
from objectifier import Objectifier
import json
from filehash import FileHash
# import allure
import pytest

config = Objectifier(json.load(open('testconfig.json')))


class Test_CURD():

    def test_generate_token(self):
        """
        Attempting to automatically generate the token
        This way may not work always as it uses a stored cookie which may expire
        """
        status = req.get_token_by_session()

    def test_one(self):
        """
        POSITIVE: Verify that a file can be uploaded successfully
        """
        file_data = {
            "name": "test_file_1.txt",
            "size": "500",
            "hash": "001a7dda7113"
        }
        response = req.upload_a_file(file_data)
        assert (response.status_code == 200)

    def test_two(self):
        """
        POSITIVE: Verify that the upload details of the file are returned correctly
        """
        file_data = {
            "name": "test_file_1.txt",
            "size": "500",
            "hash": "001a7dda7113"
        }
        response = req.upload_a_file(file_data)
        assert (response.status_code == 200)
        response_data = response.json()
        file_id = response_data['fileId']
        response = req.get_upload_status(file_id)
        assert (response.status_code == 200)
        response_data = response.json()
        assert (response_data['status'] in ['Pending', 'Completed'])

    def test_three(self):
        """
        POSITIVE: Verify that the  upload status of the file can be updated
        """
        file_data = {
            "name": "test_file_1.txt",
            "size": "500",
            "hash": "001a7dda7113"
        }
        response = req.upload_a_file(file_data)
        assert (response.status_code == 200)
        response_data = response.json()
        file_id = response_data['fileId']
        req_data = {
            'fileId': file_id,
            'bytesCompleted': "500"
        }
        response = req.update_upload_status(req_data)
        assert (response.status_code == 200)

    def test_four(self):
        """
        POSITIVE:  Verify that the files are displayed correctly for a given token
        """

        response = req.get_list_of_files_for_token()
        assert (response.status_code == 200)

    def test_five(self):
        """
        POSITIVE: Verify a file can be deleted successfully
        """
        file_data = {
            "name": "test_file_1.txt",
            "size": "500",
            "hash": "001a7dda7113"
        }
        response = req.upload_a_file(file_data)
        assert (response.status_code == 200)
        response_data = response.json()
        file_id = response_data['fileId']
        data = {
            'fileId': file_id
        }
        response = req.delete_a_file(data)
        assert (response.status_code == 200)

    def test_six(self):
        """
        POSITIVE: Verify that a file can be shared to another use
        """
        file_data = {
            "name": "test_file_1.txt",
            "size": "500",
            "hash": "001a7dda7113"
        }
        response = req.upload_a_file(file_data)
        assert (response.status_code == 200)
        response_data = response.json()
        file_id = response_data['fileId']
        data = {
            "fileId": file_id,
            "shareTo": "25"
        }
        response = req.share_a_file(data)
        assert (response.status_code == 200)

    def test_seven(self):
        """
        NEGATIVE: Verify that 401 is returned when incorrect access token is given
        """
        file_data = {
            "name": "test_file_1.txt",
            "size": "500",
            "hash": "001a7dda7113"
        }
        response = req.upload_a_file(file_data, token="1231242142143")
        assert (response.status_code == 200)

    def test_eight(self):
        """
        Verify that method not allowed is returned when trying to access APIs with incorrect method
        """
        response = req.get_list_of_files_for_token(method='POST')
        assert (response.status_code == 400)