const robot = require("robotjs");

const MICROSOFT_PAINT_LOCATION = "";
const exec = require('child_process').exec;

const MS_PAINT_PROCESS = exec('MSPaint');

MS_PAINT_PROCESS.stderr.on('data', function(data) { throw data; });
MS_PAINT_PROCESS.stdout.on('data', function(data) { throw data; });

console.log(MS_PAINT_PROCESS);

setInterval(isPaintOpenInDefaultLocation, 2000);

function isPaintOpenInDefaultLocation() {
  const mousePos = robot.getMousePos();
  const paintScreenImg = robot.screen.capture();
  console.log({
    x: mousePos.x, 
    y: mousePos.y, 
    hex: paintScreenImg.colorAt(mousePos.x, mousePos.y) });
}

/*
robot.setMouseDelay(2);
robot.moveMouse(100, 100);*/