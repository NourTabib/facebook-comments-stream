import apify_client
from facebook_scraper import get_posts

from config.db_Connexion import dbConnection
class pageScrapper:
    NUM_PAGES = 5
    def __init__(self,page_id):
        self.page_id = page_id
        self.db_cnx = dbConnection()
        self.post_collection = self.db_cnx.getCollection('posts')
        self.comments_collection = self.db_cnx.getCollection('comments')
        self.full_posts_collection = self.db_cnx.getCollection('full_posts')
        pass
    def getPosts(self):
        allPosts = []
        posts =get_posts(self.page_id,
                              pages = self.NUM_PAGES,
                              options={"progress": True},
                              #cookies=""
                        )
        self.full_posts_collection.insert_many(posts)
        for post in posts:
            if(not(post['comments'] == 0 )):
                #print(post)
                #WORKING
                print('Scrapped a Post : ',post['post_id']," From page :",self.page_id)
                allPosts.append(post['post_id'])
        return allPosts

class apifyPageScrapper:
    key ="apify_api_5XyJijSEloWpsOW92gLChi8yLbcenq00iNoO"
    actor_id = 'apify/facebook-posts-scraper'
    input_data = {
    "maxRequestRetries": 10,
    "proxy": {
        "useApifyProxy": True,
        "apifyProxyGroups": [
            "RESIDENTIAL"
        ],
        "apifyProxyCountry": "TN"
    },
    "resultsLimit": 10,
    "startUrls": [
        {
            "url": "https://www.facebook.com/Presidence.tn"
        },
        {
            "url": "https://www.facebook.com/Sntri-الشركة-الوطنية-للنقل-بين-المدن-1733465710262703"
        },
        {
            "url": "https://www.facebook.com/Wallyscar"
        },
        {
            "url": "https://www.facebook.com/MunicipaliteLaGoulette/"
        },
        {
            "url": "https://www.facebook.com/Nahda.Tunisia"
        }
    ]
    }
    def __init__(self):
        self.db_cnx = dbConnection()
        self.post_collection = self.db_cnx.getCollection('posts')
        self.client = apify_client.ApifyClient(self.key)
    def runSync(self):
        execution_info = self.client.actor(actor_id=self.actor_id).call(
            run_input=self.input_data,
            memory_mbytes = 4096,
            timeout_secs= 60*8
            )
        execution_id = execution_info['id']
        execution_details = self.client.run(run_id=execution_id).wait_for_finish()
        return execution_details
    def getItems(self,datasetId):
        execution_data = self.client.dataset(dataset_id=datasetId).list_items()
        for data in execution_data.items:
            if 'postId' in data :
                if(not(self.post_collection.find_one({"postId":data['postId']}))):
                    self.post_collection.insert_one(data)
                else:
                    pass
            elif 'postFacebookId' in data:
                if(not(self.post_collection.find_one({"postId":data['postFacebookId']}))):
                    self.post_collection.insert_one({"pageId":data['facebookId'],"postId":data['postFacebookId']})
                else:
                    pass
            else:
                pass
        return execution_data.items

