package com.liceolapaz.dam.proyectoev1di.Entities;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VideojuegoPlataformaId implements Serializable
{
    private Long idPlataforma;
    private Long idVideojuego;

    public VideojuegoPlataformaId() {}

    public VideojuegoPlataformaId(Long idPlataforma, Long idVideojuego)
    {
        this.idPlataforma = idPlataforma;
        this.idVideojuego = idVideojuego;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        VideojuegoPlataformaId that = (VideojuegoPlataformaId) o;

        return Objects.equals(idPlataforma, that.idPlataforma) && Objects.equals(idVideojuego, that.idVideojuego);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPlataforma, idVideojuego);
    }
}