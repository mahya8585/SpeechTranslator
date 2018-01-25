//接続
var fs = require('fs');
var express = require("express");
var app = express();

app.use(express.static('public'));

app.get('/favicon.ico', function(req, res) {
    res.status(204);
});

var port = process.env.PORT || 1337;
var server = app.listen(port, function() {
    console.log("Node.js is listening to PORT:" + server.address().port);
});

var io = require('socket.io').listen(server);

//redis接続
var HOST_NAME = 'xxxxxx.redis.cache.windows.net';
var AUTH = 'xxxxxxxxxxxxxxxxxx=';
var subscriber = require('redis').createClient(6380, HOST_NAME, { auth_pass: AUTH, tls: { servername: HOST_NAME } });
var publisher = require('redis').createClient(6380, HOST_NAME, { auth_pass: AUTH, tls: { servername: HOST_NAME } });;
var CHANNEL_NAME = 'meeting';

//sub設定
subscriber.subscribe(CHANNEL_NAME);
subscriber.on('message', function(channel, message) {
    console.log('channel: ' + channel + ', message: ' + message);
    io.sockets.emit('msg', { msg: message });
});

//pub設定
io.sockets.on('connection', function(socket) {
    socket.on('publisher', function(msg) {
        publisher.publish(CHANNEL_NAME, msg);
    });
});