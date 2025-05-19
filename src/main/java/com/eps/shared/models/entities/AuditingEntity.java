package com.eps.shared.models.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
@Setter
@Getter
public class AuditingEntity {
  @Id private UUID id;

  private String nguoiTao;
  private String nguoiChinhSua;

  @CreationTimestamp private LocalDateTime ngayTao;
  @UpdateTimestamp private LocalDateTime ngayChinhSua;
}
