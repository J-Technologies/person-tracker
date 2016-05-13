# Person - tracker

App made to track events on persons using cqrs.

Stack uses the following:

 - Scala
 - Axon
 - Couchbase

In the future we will support tracking of Orcs aswell

Publicly availalable documentation:

 - [Stelselcatalogus](http://www.digitaleoverheid.nl/onderwerpen/stelselinformatiepunt/stelsel-van-basisregistraties/stelselvoorzieningen/stelselcatalogus/authentieke-gegevens)
 - [Logisch ontwerp BRP](http://www.operatiebrp.nl/afnemers/logisch-ontwerp-brp)

![](http://vignette1.wikia.nocookie.net/dragons-crown/images/b/bf/Orc.png/revision/latest?cb=20140311062419)


#### Couchbase
 - Install couchbase: `brew cask install couchbase-server-enterprise`

 Helpful commands:
 - Flush bucket: `curl -X POST -u <username>:<password> http://127.0.0.1:8091/pools/default/buckets/default/controller/doFlush`