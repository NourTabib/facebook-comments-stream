import pymongo as pymongo

class dbConnection :
    def __init__(self):
        try:
            self.client = pymongo.MongoClient('mongodb://root:root@localhost:27018/')
            self.db = self.client['facebook_data']
        except:
            print("Db Connection Error")
    
    
    def getCollection(self,c_name):
        return self.db[c_name]