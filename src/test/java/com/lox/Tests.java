package com.lox;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  ScannerTest.class,
  AstPrinterTest.class,
})
public class Tests {
}
