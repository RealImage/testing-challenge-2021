using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using NUnit.Framework;
using System;
using System.Net.Http;
using System.Text;

namespace CodeChallenge
{
    public class Tests
    {
        //Variables
        static HttpClient client;
        static string Token = "1d89941d-9a67-4ed6-8ec4-eaaa8dd83609";
        static string FileID ;
        
        //Test data - Should be maintained external Json file
        JObject formData = new JObject { { "name", "TestAPI" }, { "size", 1 }, { "hash", 1 }, { "file", 1 } };
        JObject ContentFileID = new JObject { { "fileId", FileID } };

        [SetUp]
        public void Setup()
        {
            //To avoid SSL Certificate issue
            HttpClientHandler clientHandler = new HttpClientHandler();
            clientHandler.ServerCertificateCustomValidationCallback = (sender, cert, chain, sslPolicyErrors) => { return true; };

            //Creating HTTPClient instance for API call             
            client = new HttpClient(clientHandler);
            client.BaseAddress = new Uri("https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com/");

        }

        [Test, Order(1)] //Get the list of files in your account
        public void GetFilesinAccount1()
        {
            HttpResponseMessage response = client.GetAsync("sharebox/api/files?getSharedFiles=&token=" + Token).Result;
            Assert.AreEqual(200, (int)response.StatusCode, "Get API Response code validation Fail");
            Assert.AreEqual("[]", response.Content.ReadAsStringAsync().Result.ToString(), "Files already exist in account");            
        }

        [Test, Order(2)] //Upload any file into your account
        public void UploadFile()
        {
          
            HttpResponseMessage response = client.PostAsJsonAsync("sharebox/api/upload?token=" + Token, formData).Result;
            Assert.AreEqual(200, (int)response.StatusCode, "Post API Response code validation Fail");
            
            FileID = JsonConvert.DeserializeObject<FileUploadResponse>(response.Content.ReadAsStringAsync().Result.ToString()).fileId;                       
            Console.WriteLine(FileID);
        }

        [Test, Order(3)] //Check the uploaded files getting listed in your account
        public void GetFilesinAccount2()
        {
            HttpResponseMessage response = client.GetAsync("sharebox/api/files?getSharedFiles=&token=" + Token).Result;

            String ActualFileID = JsonConvert.DeserializeObject<FileResponse>(response.Content.ReadAsStringAsync().Result.ToString()).fileId;
            Assert.AreEqual(200, (int)response.StatusCode, "Get API Response code validation Fail");
            Assert.AreEqual(FileID, ActualFileID, "Uploaded file not added to account");
        }

        [Test, Order(4)] //Delete any file from your account
        public void DeleteFileFromAccount()
        {
            var request = new HttpRequestMessage
            {
                Method = HttpMethod.Delete,
                RequestUri = new Uri(client.BaseAddress + "sharebox/api/files?getSharedFiles=&token=" + Token),
                Content = new StringContent(JsonConvert.SerializeObject(ContentFileID), Encoding.UTF8, "application/json")
            };

            

            HttpResponseMessage response = client.SendAsync(request).Result;
            Assert.AreEqual(200, (int)response.StatusCode, "Delete API Response code validation Fail");

            Console.WriteLine(response.Content.ReadAsStringAsync().Result.ToString());

        }

        //Below API's are not working

        //Test 5 Create another account(same as Prerequisite) and share files from one account to another account

        //Test 6 Approve/Reject the shared file from the receiver account

    }

    //Model Files
    public class FileUploadResponse
    {
        public string secretKey { get; set; }
        public string accessKey { get; set; }
        public string sessionToken { get; set; }
        public string fileId { get; set; }
    }

    public class FileRequest
    {
        public object name { get; set; }
        public object size { get; set; }
        public object hash { get; set; }
        public object file { get; set; }
    }

    public class FileResponse
    {
        public string status { get; set; }
        public string name { get; set; }
        public string fileHash { get; set; }
        public string createdOn { get; set; }
        public int bytesCompleted { get; set; }
        public int size { get; set; }
        public string fileId { get; set; }
    }

}