package com.eps.shared.utils.functions;

@FunctionalInterface
public interface PentaConsumer<A, B, C, D, E> {

  void accept(A a, B b, C c, D d, E e);

  default PentaConsumer<A, B, C, D, E> andThen(
      PentaConsumer<? super A, ? super B, ? super C, ? super D, ? super E> after) {
    return (a, b, c, d, e) -> {
      accept(a, b, c, d, e);
      after.accept(a, b, c, d, e);
    };
  }
}
