from scrappers.pageScrapper import pageScrapper,apifyPageScrapper
from scrappers.postScrapper import postScrapper
from config.db_Connexion import dbConnection
import time
import datetime
from facebook_scraper import get_posts
import random
import json
class Application:
    def __init__(self):
        self.db_cnx = dbConnection()
        self.post_collection = self.db_cnx.getCollection('posts')
        self.comments_collection = self.db_cnx.getCollection('comments')
        self.list_of_pages = ['Nahda.Tunisia']
    def getPosts(self):
        postsList = []
        for page in self.list_of_pages:
            page_Scrapper = pageScrapper(page_id=page)
            posts = page_Scrapper.getPosts()
            for post_Id in posts:
                print(post_Id)
                if(not(self.post_collection.find_one({"post_id":post_Id}))):
                    self.post_collection.insert_one({"page_id":page,"post_id":post_Id,'timestamps':datetime.datetime.utcnow()})
            postsList.extend(posts)
        return postsList
    def getComments(self,posts,post_Scrapper):
        commentList = []
        for post_Id in posts:
            comments = post_Scrapper.Scrap(post_Id)
            for comment in comments:
                if(not(self.comments_collection.find_one({"comment_id":comment.comment_id}))):
                    self.comments_collection.insert_one({"post_id":post_Id,"comment_id":comment.comment_id,"comment_text":comment.comment_text,'timestamps':datetime.datetime.utcnow()})
                
    def run(self):
        post_Scrapper = postScrapper()
        print("Do you want to start the scrapping from scratch ? [y/n]")
        fromScratch = input()
        if(fromScratch == 'y'):
        # posts = self.getPosts()
            page_scrapper =apifyPageScrapper()
            execution_details = page_scrapper.runSync()
            page_scrapper.getItems(datasetId=execution_details['defaultDatasetId'])
        # datasets = ['UR7xVfYNE8dLaYCut']
        # for id in datasets:
            # page_scrapper.getItems(datasetId=execution_details['defaultDatasetId'])   #(datasetId=id)
            # posts = self.post_collection.find()
            # posts = [post['postId'] for post in posts]
        elif(fromScratch == 'n'):
            pass
        else:
            print("Wrong answer")
            exit()
        posts = self.post_collection.find()
        
        posts = [post['postId'] for post in posts]
        random.shuffle(posts)
        while(True):
            self.getComments(posts=posts,post_Scrapper=post_Scrapper)
            time.sleep(60*5)
        

if __name__ == "__main__":
    while(True):
        App = Application()
        App.run()
