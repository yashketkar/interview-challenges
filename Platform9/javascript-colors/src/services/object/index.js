'use strict';

const path = require('path');
const NeDB = require('nedb');
const service = require('feathers-nedb');
const hooks = require('./hooks');

module.exports = function(){
  const app = this;

  const db = new NeDB({
    filename: path.join(app.get('nedb'), 'objects.db'),
    autoload: true
  });

  let options = {
    Model: db
  };

  // Initialize our service with any options it requires
  app.use('/objects', service(options));

  // Get our initialize service to that we can bind hooks
  const objectService = app.service('/objects');

  // Set up our before hooks
  objectService.before(hooks.before);

  // Set up our after hooks
  objectService.after(hooks.after);
};
