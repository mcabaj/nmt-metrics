package com.marekcabaj;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static com.marekcabaj.NMTExtractor.COMMITTED_PROPERTY;
import static com.marekcabaj.NMTExtractor.RESERVED_PROPERTY;
import static org.junit.Assert.assertEquals;

public class NMTExtractorTest {

    private NMTExtractor nmtExtractor;

    @Before
    public void setUp() {
        String testJmcdOutput = "Total: reserved=1470626KB, committed=170826KB\n" +
                "-                 Java Heap (reserved=65536KB, committed=46592KB)\n" +
                "                            (mmap: reserved=65536KB, committed=46592KB) \n" +
                " \n" +
                "-                     Class (reserved=1081294KB, committed=36814KB)\n" +
                "                            (classes #5962)\n" +
                "                            (malloc=4046KB #6901) \n" +
                "                            (mmap: reserved=1077248KB, committed=32768KB) \n" +
                " \n" +
                "-                    Thread (reserved=22009KB, committed=22009KB)\n" +
                "                            (thread #22)\n" +
                "                            (stack: reserved=21504KB, committed=21504KB)\n" +
                "                            (malloc=65KB #112) \n" +
                "                            (arena=440KB #42)\n" +
                " \n" +
                "-                      Code (reserved=252309KB, committed=16101KB)\n" +
                "                            (malloc=2709KB #3757) \n" +
                "                            (mmap: reserved=249600KB, committed=13392KB) \n" +
                " \n" +
                "-                        GC (reserved=6028KB, committed=5860KB)\n" +
                "                            (malloc=3468KB #184) \n" +
                "                            (mmap: reserved=2560KB, committed=2392KB) \n" +
                " \n" +
                "-                  Compiler (reserved=8424KB, committed=8424KB)\n" +
                "                            (malloc=9KB #111) \n" +
                "                            (arena=8415KB #8)\n" +
                " \n" +
                "-                  Internal (reserved=4155KB, committed=4155KB)\n" +
                "                            (malloc=4091KB #7583) \n" +
                "                            (mmap: reserved=64KB, committed=64KB) \n" +
                " \n" +
                "-                    Symbol (reserved=9378KB, committed=9378KB)\n" +
                "                            (malloc=6557KB #58783) \n" +
                "                            (arena=2821KB #1)\n" +
                " \n" +
                "-    Native Memory Tracking (reserved=1232KB, committed=1232KB)\n" +
                "                            (malloc=5KB #66) \n" +
                "                            (tracking overhead=1227KB)\n" +
                " \n" +
                "-               Arena Chunk (reserved=20262KB, committed=20262KB)\n" +
                "                            (malloc=20262KB) ";
        nmtExtractor = new NMTExtractor(testJmcdOutput);
    }

    @Test
    public void testGetTotal() throws Exception {
        Map<String, Integer> properties = nmtExtractor.getNMTProperties().get("total");
        assertEquals(1470626, properties.get(RESERVED_PROPERTY).intValue());
        assertEquals(170826, properties.get(COMMITTED_PROPERTY).intValue());
    }

    @Test
    public void testGetHeap() throws Exception {
        Map<String, Integer> properties = nmtExtractor.getNMTProperties().get("java.heap");
        assertEquals(65536, properties.get(RESERVED_PROPERTY).intValue());
        assertEquals(46592, properties.get(COMMITTED_PROPERTY).intValue());
    }

    @Test
    public void testGetClass() throws Exception {
        Map<String, Integer> properties = nmtExtractor.getNMTProperties().get("class");
        assertEquals(1081294, properties.get(RESERVED_PROPERTY).intValue());
        assertEquals(36814, properties.get(COMMITTED_PROPERTY).intValue());
    }

    @Test
    public void testGetThread() throws Exception {
        Map<String, Integer> properties = nmtExtractor.getNMTProperties().get("thread");
        assertEquals(22009, properties.get(RESERVED_PROPERTY).intValue());
        assertEquals(22009, properties.get(COMMITTED_PROPERTY).intValue());
    }

    @Test
    public void testGetCode() throws Exception {
        Map<String, Integer> properties = nmtExtractor.getNMTProperties().get("code");
        assertEquals(252309, properties.get(RESERVED_PROPERTY).intValue());
        assertEquals(16101, properties.get(COMMITTED_PROPERTY).intValue());
    }

    @Test
    public void testGC() throws Exception {
        Map<String, Integer> properties = nmtExtractor.getNMTProperties().get("gc");
        assertEquals(6028, properties.get(RESERVED_PROPERTY).intValue());
        assertEquals(5860, properties.get(COMMITTED_PROPERTY).intValue());
    }

    @Test
    public void testGetCompiler() throws Exception {
        Map<String, Integer> properties = nmtExtractor.getNMTProperties().get("compiler");
        assertEquals(8424, properties.get(RESERVED_PROPERTY).intValue());
        assertEquals(8424, properties.get(COMMITTED_PROPERTY).intValue());
    }

    @Test
    public void testGetInternal() throws Exception {
        Map<String, Integer> properties = nmtExtractor.getNMTProperties().get("internal");
        assertEquals(4155, properties.get(RESERVED_PROPERTY).intValue());
        assertEquals(4155, properties.get(COMMITTED_PROPERTY).intValue());
    }

    @Test
    public void testGetSymbol() throws Exception {
        Map<String, Integer> properties = nmtExtractor.getNMTProperties().get("symbol");
        assertEquals(9378, properties.get(RESERVED_PROPERTY).intValue());
        assertEquals(9378, properties.get(COMMITTED_PROPERTY).intValue());
    }

    @Test
    public void testGetNMT() throws Exception {
        Map<String, Integer> properties = nmtExtractor.getNMTProperties().get("native.memory.tracking");
        assertEquals(1232, properties.get(RESERVED_PROPERTY).intValue());
        assertEquals(1232, properties.get(COMMITTED_PROPERTY).intValue());
    }

    @Test
    public void testGetArenaChunk() throws Exception {
        Map<String, Integer> properties = nmtExtractor.getNMTProperties().get("arena.chunk");
        assertEquals(20262, properties.get(RESERVED_PROPERTY).intValue());
        assertEquals(20262, properties.get(COMMITTED_PROPERTY).intValue());
    }
}