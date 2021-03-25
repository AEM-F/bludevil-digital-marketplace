import { FirstToUpperPipe } from './first-to-upper.pipe';

describe('FirstToUpperPipe', () => {
  it('create an instance', () => {
    const pipe = new FirstToUpperPipe();
    expect(pipe).toBeTruthy();
  });
});
