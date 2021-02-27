# Tips
Here are some small tips and tricks you can use. If you have some tips for me or your fellow students, please feel free to create a pull request (or simply tell me) and we can add them here!

## Testing on IFI machines
The file *Testing on IFI Machines* is a guide to how you can test your programs on IFI machines. Included is how to test your program on a machine called `margir`, that boasts 64 cores!

## Testing output of programs
If your terminal supports it, you can use the `diff` command to compare the output of programs and contents of text files.

Comparing output of two programs:

```
diff <(program1) <(program2)
```

Comparing output of a program against the content of a text file (the order doesn't matter):

```
diff <(program1) filepath
```

For example for oblig 3, you can use the text files in [test_utils](../oblig_sequential_solutions/oblig3/test_utils) to compare against the output of your program (given that you print one prime on each line):

```
diff <(java SieveOfEratosthenes 100) test_utils/primes100.txt
```
If they are the same, `diff` returns nothing (this is what we want). If they are not the same, `diff` gives feedback on which lines are different.
