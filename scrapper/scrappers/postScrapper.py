import json
from config.db_Connexion import dbConnection
from facebook_scraper import get_posts
from models.Comment import Comment

class postScrapper:
    MAX_COMMENTS = 1000
    def __init__(self):
        self.db_cnx = dbConnection()
        self.post_with_reaction_collection = self.db_cnx.getCollection('posts_with_reactions')
        self.raw_comments = self.db_cnx.getCollection('raw_comments')
    def scrapPost(self,post_id = None):
        if(post_id == None):
            return None
        if(self.post_with_reaction_collection.find_one({'post_id':post_id})):
            return None
        generator = get_posts(
            post_urls=[post_id],
            extra_info=True,
            timeout=60,   
            options={"comments": self.MAX_COMMENTS, "progress": True,"reactors": True },
            cookies=""
            )
        requested_post = next(generator)
        if('reactions' in requested_post):
            self.post_with_reaction_collection.insert_one(requested_post)
        return requested_post
    def getComments(self,post = None):
        #print("in get Comments")
        allComments = []
        if(post == None):
            return []
        if(not('comments_full' in post)):
            return []
        comments = post['comments_full']
        for comment_interator in comments:
            print("full comments")
            print(comment_interator)
            self.raw_comments.insert_one(comment_interator)
            print("Got a comments : ",comment_interator['comment_id'])
            comment = Comment(
                id =comment_interator['comment_id'],
                text =comment_interator['comment_text']
            )
            allComments.append(comment)
            if(comment_interator['replies']):
                for reply_iterator in comment_interator['replies']:
                    reply = Comment(
                    id = reply_iterator['comment_id'],
                    text = reply_iterator['comment_text']
                        )
                    allComments.append(reply)
        return allComments
    def Scrap(self,post_Id):
        post = self.scrapPost(post_id=post_Id)
        return self.getComments(post=post)
