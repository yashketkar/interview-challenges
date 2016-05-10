'use strict';

const assert = require('assert');
const app = require('../../../src/app');

describe('object service', () => {
  it('registered the objects service', () => {
    assert.ok(app.service('objects'));
  });
});
