#! /usr/bin/env bash

curl -XDELETE 'http://cmput301f15t03.westus.cloudapp.azure.com:9200/cmput301f15t03/UserProfile/joe'
printf "\ndeleted joe: UserProfile\n\n"
curl -XDELETE 'http://cmput301f15t03.westus.cloudapp.azure.com:9200/cmput301f15t03/Inventory/joe'
printf "\ndeleted joe: Inventory\n\n"
curl -XDELETE 'http://cmput301f15t03.westus.cloudapp.azure.com:9200/cmput301f15t03/FriendsList/joe'
printf "\ndeleted joe: FriendsList\n\n"
curl -XDELETE 'http://cmput301f15t03.westus.cloudapp.azure.com:9200/cmput301f15t03/TradeList/joe'
printf "\ndeleted joe: TradeList\n\n"
curl -XDELETE 'http://cmput301f15t03.westus.cloudapp.azure.com:9200/cmput301f15t03/TradeList/bob'
printf "\ndeleted bob: TradeList\n\n"
curl -XDELETE 'http://cmput301f15t03.westus.cloudapp.azure.com:9200/cmput301f15t03/Trade'
printf "\ndeleted Trade type\n\n"
