const robot = require("robotjs");

const MICROSOFT_PAINT_LOCATION = "";

const exec = require('child_process').exec;

exec('MSPaint', function callback(error, stdout, stderr) {
  if (error) {
    throw error;
  }
  
  if (stderr) {
    throw stder;
  }
  
  console.log('MS Paint finished successfully');
});

/*
robot.setMouseDelay(2);
robot.moveMouse(100, 100);*/