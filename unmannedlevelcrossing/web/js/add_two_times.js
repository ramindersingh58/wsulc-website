/**
 * Sum two times values HH:mm:ss with javascript
 * Usage:
 *  > addTimes('04:20:10', '21:15:10');
 *  > "25:35:20"
 *
 * @param {string} start
 * @param {string} end
 * @returns {String}
 */
function addTimes(start, end) {
  times = [];
  times1 = start.split(':');
  times2 = end.split(':');

  for (var i = 0; i < 3; i++) {
    times1[i] = (isNaN(parseInt(times1[i]))) ? 0 : parseInt(times1[i])
    times2[i] = (isNaN(parseInt(times2[i]))) ? 0 : parseInt(times2[i])
    times[i] = times1[i] + times2[i];
  }

  var seconds = times[2];
  var minutes = times[1];
  var hours = times[0];

  if (seconds % 60 === 0) {
    hours += seconds / 60;
  }

  if (minutes % 60 === 0) {
    res = minutes / 60;
    hours += res;
    minutes = minutes - (60 * res);
  }

  return hours + ':' + minutes + ':' + seconds;
}