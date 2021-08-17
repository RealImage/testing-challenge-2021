import requests


class Constants:

    @staticmethod
    def get_all_files(token):
        return "https://ec2-15-206-185-35.ap-south-1.compute.amazonaws.com/" \
               "sharebox/api/files?token=" + token

    @staticmethod
    def upload_file(token):
        return "https://ec2-15-206-185-35.ap-south-1.compute.amazonaws.com/" \
               "sharebox/api/upload?token=" + token

    @staticmethod
    def get_individual_file(token, file_id):
        return "https://ec2-15-206-185-35.ap-south-1.compute.amazonaws.com/" \
               f"sharebox/api/upload?fileId={file_id}&token={token}"

    @staticmethod
    def delete_file(token):
        return "https://ec2-15-206-185-35.ap-south-1.compute.amazonaws.com/" \
               "sharebox/api/files?token=" + token


class Methods:

    @staticmethod
    def get_all_files(url, file_count, testcase=None):
        try:
            response_body = requests.get(url, verify=False).json()
            file_count_check = file_count == len(response_body)
            if testcase:
                result_str = " File Count Matches" if file_count_check else "File Count Mismatches"
                print("Step: " + str(testcase) + " " + result_str)
            return file_count_check
        except Exception as e:
            print("ERROR: ", e)

    @staticmethod
    def upload_file(url, data, token, header, testcase=None):
        try:
            response_body = requests.post(url, data=data, headers=header, verify=False).json()
            file_id = response_body["fileId"]
            upload_check = requests.get(Constants.get_individual_file(token=token,
                                                                      file_id=file_id),
                                        verify=False).status_code
            if testcase:
                result_str = "Upload Success" if upload_check == 200 else "Upload Failed"
                print("Step: " + str(testcase) + " " + result_str)
            return upload_check
        except Exception as e:
            print("ERROR: ", e)

    @staticmethod
    def delete_file(url, file_id, data, token, header, testcase=None):
        try:
            response_body = requests.delete(url, data=data, headers=header, verify=False).json()
            message = "File deleted successfully" == response_body["message"]
            delete_check = requests.get(Constants.get_individual_file(token=token,
                                                                      file_id=file_id),
                                        verify=False).status_code
            if testcase:
                result_str = "Deletion Success" if (
                        delete_check == 404 and message) else "Deletion Failed"
                print("Step: " + str(testcase) + " " + result_str)
            return delete_check
        except Exception as e:
            print("ERROR: ", e)


class ShareBoxApi(Methods):

    def __init__(self):
        self.token = "ee101d89-0b17-43ae-9d1a-ac570c74b401"

    def tc_share_box_api_test(self):
        # Step 1: Get the list of files in your account
        url = Constants.get_all_files(self.token)
        self.get_all_files(url=url, file_count=0, testcase=1)

        # Step 2: Upload any file into your account and Check the uploaded files getting
        # listed in your account
        values = """
                    --BOUNDARY
                    Content-Disposition: form-data; name="name"
                    --BOUNDARY
                    Content-Disposition: form-data; name="size"
                    --BOUNDARY
                    Content-Disposition: form-data; name="hash"
                    --BOUNDARY
                    Content-Disposition: form-data; name="file"
                    --BOUNDARY--
                    """
        headers = {
            'Content-Type': 'multipart/form-data; boundary=BOUNDARY',
            'Accept': 'application/json'
        }
        url = Constants.upload_file(token=self.token)
        self.upload_file(url=url, data=values, header=headers, token=self.token, testcase=2)

        # Step 3: Delete any file from your account
        url = Constants.delete_file(token=self.token)
        values = """
                    --BOUNDARY
                    Content-Disposition: form-data; name="fileId"
                    --BOUNDARY--
                    """
        headers = {
            'Content-Type': 'multipart/form-data; boundary=BOUNDARY',
            'Accept': 'application/json'
        }
        self.delete_file(url=url, file_id=1, token=self.token, header=headers, data=values,
                         testcase=3)


if __name__ == "__main__":
    share_box = ShareBoxApi()
    share_box.tc_share_box_api_test()
