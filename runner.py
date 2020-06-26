import hashlib
import os
from pathlib import Path

from share_box import upload, files


def main():
    """Take the input from the user and validate."""
    print('=' * 50)
    print('Share Box Automation')
    print('=' * 50)
    print('1. Detail of uploaded file')
    print('2. Upload file')
    print('3. Update a file')
    print('4. List all the files')
    print('5. Delete file')
    print('6. Share file to others.')
    print('7. Approve/Reject file from other user')
    print('=' * 50)
    resp = input('Enter your choice: ')
    if resp == '1':
        get_uploadfile_detail()
    elif resp == '2':
        upload_file()
    elif resp == '3':
        check_update()
    elif resp == '4':
        list_files()
    elif resp == '5':
        delete_file()
    elif resp == '6':
        share_file()
    elif resp == '7':
        resp_share_file()
		
    else:
        print("Wrong choice selected.")
        print("Exiting!!!")

def get_uploadfile_detail():
    """Hits api and fetches all the files."""
    file_id = input("Enter a file id: ")
    resp = upload.get_upload_file_details(file_id)
    print(resp)
	
def list_files():
    """Hits api and fetches all the files."""
    resp = files.get_all_files()
    print(resp)


def upload_file():
    """Upload a file by taking input from the user."""
    file_ = input('Enter file: ')
    if not os.path.isfile(file_):
        print(f"File not exist: {file_}")
    else:
        file_name = file_
        file_size = Path(file_).stat().st_size
        file_hash = hash_file(file_name)
        fh = open(file_, 'r')
        with open(file_ ,'rb') as fh:
            data = fh.read()

        resp = upload.upload_file(file_name, file_size, file_hash, data)
        print(resp)


def check_update():
    """Check the update of the file."""
    file_id = input("Enter a file id: ")
    resp = upload.update_file_status(file_id)
    print(resp)


def delete_file():
    """Deletes the file from share box."""
    file_id = input("Enter a file id: ")
    resp = files.delete_file(file_id)
    print(resp)


def share_file():
    """Share a file with other user."""
    file_id = input("Enter a file id: ")
    user_name = input("Enter user name:")
    resp = files.share_file(file_id, user_name)
    print(resp)


def resp_share_file():
    """Accept/Reject file from the other user."""
    file_id = input("Enter a file id: ")
    resp = input("Accept the file? (Y/N):")
    if resp and resp.isalpha() and resp.lower() == 'y':
        resp = files.resp_share_file(file_id, True)
    else:
        resp = files.resp_share_file(file_id, False)
    print(resp)


def hash_file(filename):
    """This function returns the SHA-1 hash of the file passed into it

    :parameter
    filename (str): file to be hashed.

    :return
    str: file hash.
    """
    h = hashlib.sha1()

    with open(filename,'rb') as file:
        chunk = 0
        while chunk != b'':
            chunk = file.read(1024)
            h.update(chunk)

    return h.hexdigest()


if __name__ == '__main__':
    main()
