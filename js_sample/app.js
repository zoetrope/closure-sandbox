goog.provide('example.app');

goog.require('example.templates.hello');
goog.require('soy');
goog.require('goog.net.XhrIo');

example.app = function() {
  goog.net.XhrIo.send('/data.json', function(e) {
    var xhr = e.target;
    if(xhr.isSuccess) {
      var data = xhr.getResponseJson();
      var fragment = soy.renderAsFragment(example.templates.hello.render, data);
      document.body.appendChild(fragment);
    }
  });
}();

