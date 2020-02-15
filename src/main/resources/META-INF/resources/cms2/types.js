// @flow

/** Casts an object to an arbitrary type. */
export function cast/*:: <T>*/(x /*: ?Object*/) /*: T*/ {
  if (x === null || typeof x === 'undefined') {
    throw new Error("unexpected null or undefined");
  } else {
    /*:: (x: T); */
    return x;
  }
}
