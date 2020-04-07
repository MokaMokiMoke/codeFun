## 2016-11-29

- At first the threadfull version was slower by 5 times
- Improved speed of the non-threadfull version by 5 times with "don´t use autocommit"
- Changed the strcuture to put all data into a stream and afterwards into a array instead of reading it line by line (slow access)
- Optimized the threadfull version -> up to 10 times faster than the non-threadfull
- Changed the insertStatement to a StringBuilder for better overview
- Made a small cleanup with connection.close()

### Speed comparison (10k Inserts)
Non-Threadfull:
- Init version (autoCommit(true) && single Threaded): 13.902 ms
- Slow 1 Threaded Edition: 38.889 ms
- Slow 2 Threaded Edition: 29.635 ms
- Slow 4 Threaded Edition: 25.455 ms
- Init version without autoCommit: 2.092 ms

Threadfull with autoCommit(true):
- 1 Thread: 14.531 ms
- 2 Threads: 13.240 ms
- 5 Threads: 8.059 ms
- 10 Threads: 6.286 ms

Threadfull with autoCommit(false):
- 20 Threads: 2.673 (best number of threads)

10.000.000 Inserts:
- No threadfull version with autoCommit(false): 2.440.748 ms
- 20 Threads with autoCommit(false): 


## Summary
- With both optimizations i got a 20 - 50 times speedup
