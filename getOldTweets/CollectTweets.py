# -*- coding: utf-8 -*-
import sys, getopt, datetime, codecs
from pymongo import MongoClient

if sys.version_info[0] < 3:
    import got
else:
    import got3 as got


def main(argv):
    if len(argv) == 0:
        print('You must pass some parameters. Use \"-h\" to help.')
        return

    if len(argv) == 1 and argv[0] == '-h':
        f = open('exporter_help_text.txt', 'r')
        print f.read()
        f.close()

        return

    try:
        opts, args = getopt.getopt(argv, "", (
        "username=", "near=", "within=", "since=", "until=", "querysearch=", "toptweets", "maxtweets=", "output=", "language="))

        tweetCriteria = got.manager.TweetCriteria()
        client = MongoClient(port=27017)
        db = client.tweets

        for opt, arg in opts:
            if opt == '--username':
                tweetCriteria.username = arg

            elif opt == '--since':
                tweetCriteria.since = arg

            elif opt == '--until':
                tweetCriteria.until = arg

            elif opt == '--language':
                tweetCriteria.language = arg

            elif opt == '--querysearch':
                tweetCriteria.querySearch = arg

            elif opt == '--toptweets':
                tweetCriteria.topTweets = True

            elif opt == '--maxtweets':
                tweetCriteria.maxTweets = int(arg)

            elif opt == '--near':
                tweetCriteria.near = '"' + arg + '"'

            elif opt == '--within':
                tweetCriteria.within = '"' + arg + '"'

            elif opt == '--within':
                tweetCriteria.within = '"' + arg + '"'

        print('Searching...\n')

        def receiveBuffer(tweets):
            for t in tweets:

                tweet = {
                    'username': t.username,
                    'message': t.text,
                    'date': t.date.strftime("%Y-%m-%d %H:%M"),
                    'language' : tweetCriteria.language
                }

                print(tweet)
                result = db.tweet.insert_one(tweet)

            print('More %d saved in MongoDB.\n' % len(tweets))

        got.manager.TweetManager.getTweets(tweetCriteria, receiveBuffer)

    except arg:
        print('Arguments parser error, try -h' + arg)
    finally:
        print('Finally')



if __name__ == '__main__':
    main(sys.argv[1:])
