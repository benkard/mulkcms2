// @flow

export function cast/*:: <T>*/(x /*: ?Object*/) /*: T*/ {
  if (x === null || x === undefined) {
    throw "unexpected null or undefined";
  } else {
    /*:: (x: T); */
    return x;
  }
}
