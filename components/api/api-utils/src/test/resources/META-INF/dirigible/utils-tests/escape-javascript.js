
import { Escape } from 'sdk/utils/escape';
import { Assert } from 'test/assert';

const input = 'javascript \t characters \n';
const result = Escape.escapeJavascript(input);

Assert.assertEquals(result, 'javascript \\t characters \\n');
