import re
import nltk
import pickle
from urllib import urlopen
import json

url ="http://en.wikipedia.org/w/api.php?cmtitle=Category:Visitor_attractions_in_Copenhagen&action=query&list=categorymembers&cmlimit=500&cmprop=title|sortkey|timestamp&format=json"
out = urlopen(url).read()
#out = out[1:len(out)-1]
print out[:10]

JsonObject = json.loads(out)

attractionJsonList = []
for attraction in JsonObject['query']['categorymembers']:
    title = attraction['title']
    title = title.replace(' ', '_')
    url = 'http://en.wikipedia.org/w/api.php?format=json&action=query&titles=%s&prop=revisions&rvprop=content' %title
    url = url.encode('utf-8')
    print url
    attractionPage = urlopen(url).read()
    attractionJson = json.loads(attractionPage)
    attractionJsonList.append(attractionJson)
    #print attractionJson['query']


#for attractionJson in 

    
print 'Done loading data'

url = "http://en.wikipedia.org/w/api.php?format=txt&action=query&titles=batman&prop=revisions&rvprop=content"
