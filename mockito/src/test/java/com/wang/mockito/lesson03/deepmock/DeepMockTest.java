package com.wang.mockito.lesson03.deepmock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @description: 深层Mock
 * @author: wei·man cui
 * @date: 2020/8/13 13:58
 */
public class DeepMockTest {

    @Mock
    private Lesson03Service lesson03Service;

    /**
     * 深层Mock
     */
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Lesson03Service lesson03Service2;

    @Mock
    private Lesson03 lesson03;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMock() {
        Lesson03 l03 = lesson03Service.get();
        l03.foo();
    }

    @Test
    public void testDeepMock() {
        Mockito.when(lesson03Service.get()).thenReturn(lesson03);
        Lesson03 l03 = lesson03Service.get();
        l03.foo();
    }

    @Test
    public void testDeepMockByAnswers() {
        Lesson03 l03 = lesson03Service2.get();
        l03.foo();
    }

}
