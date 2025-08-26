package br.comtotempoo.totempoo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.comtotempoo.totempoo.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByTipo(String tipo);
}
